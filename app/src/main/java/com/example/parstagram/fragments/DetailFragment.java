package com.example.parstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.parstagram.R;

public class DetailFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvCreatedAt;
    private ImageView ivImage;
    private TextView tvDescription;

    public DetailFragment() {
        //required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
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
