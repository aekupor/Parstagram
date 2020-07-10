package com.example.parstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parstagram.R;

public class FollowersFragment extends Fragment {

    public static final String TAG = "FollowersFragment";

    public FollowersFragment() {
        // Required empty public constructor
    }

    public static FollowersFragment newInstance() {
        FollowersFragment followersFragment = new FollowersFragment();
        Bundle args = new Bundle();
        followersFragment.setArguments(args);
        return followersFragment;
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

    }

}
