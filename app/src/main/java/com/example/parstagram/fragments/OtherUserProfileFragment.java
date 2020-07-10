package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.parstagram.Query;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.example.parstagram.adapters.PostsAdapter;
import com.example.parstagram.R;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    private User user;
    private TextView tvNumFollowers;
    private TextView tvNumFollowing;
    private TextView tvFollowersTitle;
    private Button btnFollow;

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
        tvNumFollowing = view.findViewById(R.id.tvNumFollowing);
        btnFollow = view.findViewById(R.id.btnFollow);

        query = new Query();
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

                User currentUser = (User) ParseUser.getCurrentUser();
                ParseRelation<ParseObject> relation = currentUser.getRelation(KEY_FOLLOWERS);
                if (btnFollow.getText().toString().equals("Follow")) {
                    relation.add(user);
                    btnFollow.setText("Unfollow");
                } else {
                    relation.remove(user);
                    btnFollow.setText("Follow");
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
        query.findFollowers(user, new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error getting followers");
                }
                Log.i(TAG, "got followers");
                for (User user : objects) {
                    Log.i(TAG, "user: " + user.getUsername());
                }
                tvNumFollowers.setText(String.valueOf(objects.size()));
            }
        });
    }

    private void findFollowing() {
        //TODO: this isn't actually finding num of following, it is finding total num of users
        ParseQuery<User> query = ParseQuery.getQuery("_User");
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error getting following");
                }
                Log.i(TAG, "got following");
                for (User user : objects) {
                    Log.i(TAG, "FOLLOWING: " + user.getUsername());
                }
                tvNumFollowing.setText(String.valueOf(objects.size()));
            }
        });
    }

    private void queryUser() {
        query.queryUser(userId, new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting user", e);
                    return;
                }
                user = users.get(0);
                Log.i(TAG, "User: " + user.getUsername());
                tvUsername.setText(user.getUsername());

                if (user.getParseFile("profileImage") != null) {
                    Glide.with(getContext())
                            .load(user.getParseFile("profileImage").getUrl())
                            .into(ivProfileImage);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.ic_baseline_person_outline_24)
                            .into(ivProfileImage);
                }

                findFollowing();
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
