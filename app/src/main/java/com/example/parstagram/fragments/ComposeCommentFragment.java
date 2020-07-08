package com.example.parstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.parstagram.R;

public class ComposeCommentFragment extends DialogFragment {

    private EditText tvCommentText;

    public ComposeCommentFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static ComposeCommentFragment newInstance(String text) {
        ComposeCommentFragment frag = new ComposeCommentFragment();
        Bundle args = new Bundle();
        args.putString("text", text);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_comment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvCommentText = (EditText) view.findViewById(R.id.tvCommentDescription);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("text", "");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        tvCommentText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}