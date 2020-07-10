package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parstagram.Query;
import com.example.parstagram.R;
import com.example.parstagram.adapters.FollowerAdapter;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FollowersFragment extends Fragment {

    public static final String TAG = "FollowersFragment";
    public static final String KEY_FOLLOWERS = "followers";

    private String userId;
    private User user;

    private RecyclerView rvComments;
    private FollowerAdapter adapter;
    private List<User> allUsers;
    private Query query;

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment newInstance(String userId) {
        FollowersFragment followersFragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        followersFragment.setArguments(args);
        return followersFragment;
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
        return inflater.inflate(R.layout.fragment_followers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvComments = view.findViewById(R.id.rvFollowers);
        query = new Query();

        allUsers = new ArrayList<User>();
        adapter = new FollowerAdapter(getContext(), allUsers);

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        queryUser();
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
                findFollowers();
            }
        });
    }

    private void findFollowers() {
        //find followers of user
        ParseQuery<User> query = ParseQuery.getQuery("_User");
        query.whereEqualTo(KEY_FOLLOWERS, user);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error getting followers");
                }
                Log.i(TAG, "got followers");
                for (User user : objects) {
                    Log.i(TAG, "user: " + user.getUsername());
                }
                allUsers.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
