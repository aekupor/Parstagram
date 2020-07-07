package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parstagram.Post;
import com.example.parstagram.R;

import org.parceler.Parcels;

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";

    private TextView tvUsername;
    private TextView tvCreatedAt;
    private ImageView ivImage;
    private TextView tvDescription;

    private String postId;

    public static DetailFragment newInstance(String postId) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("postId", postId);
        detailFragment.setArguments(args);
        return detailFragment;
    }

    public DetailFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getString("postId", "");
        Log.i(TAG, "post id: " + postId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsernameDetail);
        tvCreatedAt = view.findViewById(R.id.tvCreatedAtDetail);
        ivImage = view.findViewById(R.id.ivImageDetail);
        tvDescription = view.findViewById(R.id.tvDescriptionDetail);


    }
}
