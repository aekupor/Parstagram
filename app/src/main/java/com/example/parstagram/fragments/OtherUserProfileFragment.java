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
import com.example.parstagram.ProfileImage;
import com.example.parstagram.Query;
import com.example.parstagram.models.Post;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class OtherUserProfileFragment extends Fragment {

    public static final String TAG = "OtherUserProfileFragment";
    public static final String KEY_FOLLOWERS = "followers";

    private RecyclerView rvPosts;
    private PostsAdapter adapter;
    private List<Post> allPosts;
    private Query query;

    private TextView tvUsername;
    private ImageView ivProfileImage;
    private String userId;
    private Button btnChat;
    private ParseUser user;
    private TextView tvNumFollowers;
    private TextView tvFollowersTitle;
    private Button btnFollow;
    private Boolean follow;

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
        tvFollowersTitle = view.findViewById(R.id.tvFollowersTitle);
        btnFollow = view.findViewById(R.id.btnFollow);

        query = new Query();
        follow = false;
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


        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "follow button clicked");

                ParseUser currentUser = ParseUser.getCurrentUser();
                ParseRelation<ParseObject> relation = currentUser.getRelation(KEY_FOLLOWERS);
                if (!follow) {
                    relation.add(user);
                } else {
                    relation.remove(user);
                }

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.i(TAG, "followers update unsuccessful");
                        }
                        Log.i(TAG, "followers update successful");
                        findFollowers();
                    }
                });
            }
        });

        tvFollowersTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFollowers();
            }
        });

        tvNumFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listFollowers();
            }
        });

        queryUser();
    }

    private void listFollowers() {
        Log.i(TAG, "list followers clicked");
        // go to Followers Fragment
        final FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        Fragment fragment = FollowersFragment.newInstance(userId);
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void findFollowers() {
        query.findFollowers(user, new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error getting followers");
                }
                Log.i(TAG, "got followers");
                for (ParseUser user : objects) {
                    Log.i(TAG, "user: " + user.getUsername());
                }
                tvNumFollowers.setText(String.valueOf(objects.size()));
            }
        });
    }

    private void queryUser() {
        query.queryUser(userId, new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting user", e);
                    return;
                }
                user = users.get(0);
                Log.i(TAG, "User: " + user.getUsername());
                tvUsername.setText(user.getUsername());

                ProfileImage getProfile = new ProfileImage();
                Glide.with(getContext())
                        .load(getProfile.getProfileImage(user))
                        .into(ivProfileImage);

                findFollowers();
                queryPosts();
            }
        });
    }

    protected void queryPosts() {
        query.queryPosts(user, new FindCallback<Post>() {
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
