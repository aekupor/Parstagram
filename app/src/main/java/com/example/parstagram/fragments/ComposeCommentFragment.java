package com.example.parstagram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.parstagram.R;

public class ComposeCommentFragment extends DialogFragment {

    EditText etCommentText;
    Button btnDone;

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
        etCommentText = view.findViewById(R.id.etComment);
        btnDone = view.findViewById(R.id.btnCommentDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return input text back to activity through the implemented listener
                ComposeCommentDialogListener listener = (ComposeCommentDialogListener) getTargetFragment();
                listener.onFinishEditDialog(etCommentText.getText().toString());
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }
}