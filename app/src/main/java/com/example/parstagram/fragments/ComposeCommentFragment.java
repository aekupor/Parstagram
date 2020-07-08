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

    public static ComposeCommentFragment newInstance() {
        ComposeCommentFragment frag = new ComposeCommentFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    // Defines the listener interface
    public interface ComposeCommentDialogListener {
        void onFinishEditDialog(String inputText);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        tvCommentText = (EditText) view.findViewById(R.id.tvCommentDescription);
        // Show soft keyboard automatically and request focus to field
        tvCommentText.requestFocus();
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        ComposeCommentDialogListener listener = (ComposeCommentDialogListener) getTargetFragment();
        listener.onFinishEditDialog(tvCommentText.getText().toString());
        dismiss();
    }
}