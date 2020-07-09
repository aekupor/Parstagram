package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileFragment extends Fragment {

    public static final String TAG = "OtherUserProfileFragment";

    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> allPosts;

    private TextView tvUsername;
    private ImageView ivProfileImage;
    private String userId;
    private Button btnChat;
    private User user;
    private TextView tvNumFollowers;

    public OtherUserProfileFragment() {
        // Required empty public constructor
    }

    public static OtherUserProfileFragment newInstance(String userId) {
        OtherUserProfileFragment otherUserProfileFragment = new OtherUserProfileFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        otherUserProfileFragment.setArguments(args);
        return otherUserProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("userId", "");
        Log.i(TAG, "user id: " + userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otheruser_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvOtherProfilePosts);
        tvUsername = view.findViewById(R.id.tvOtherProfileUsername);
        ivProfileImage = view.findViewById(R.id.ivOtherProfileImage);
        btnChat = view.findViewById(R.id.btnChat);
        tvNumFollowers = view.findViewById(R.id.tvNumFollowers);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        rvPosts.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvPosts.setLayoutManager(gridLayoutManager);


        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "chat button clicked");
                // go to Chat Fragment
                final FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                Fragment fragment = ChatFragment.newInstance();
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            }
        });

        queryUser();
    }

    private void queryUser() {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereMatches("objectId", userId);
        query.findInBackground(new FindCallback<User>() {
            public void done(List<User> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting user", e);
                    return;
                }
                user = users.get(0);
                Log.i(TAG, "User: " + user.getUsername());

                tvUsername.setText(user.getUsername());
                if (user.getFollowers() == null) {
                    tvNumFollowers.setText("0");
                } else {
                    tvNumFollowers.setText(user.getFollowers().size());
                }

                if (user.getParseFile("profileImage") != null) {
                    Glide.with(getContext())
                            .load(user.getParseFile("profileImage").getUrl())
                            .into(ivProfileImage);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.ic_baseline_person_outline_24)
                            .into(ivProfileImage);
                }
                queryPosts();
            }
        });
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
