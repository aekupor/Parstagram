package com.example.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.parstagram.R;
import com.example.parstagram.models.Comment;
import com.parse.ParseException;

import java.util.List;

public class FollowerAdapter extends CommentAdapter {

    private Context context;

    public FollowerAdapter(Context context, List<Comment> comments) {
        super(context, comments);
    }

    class CommentViewHolder extends ViewHolder {

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Comment comment) {
            try {
                tvUsername.setText(comment.getByUser().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            }

            if (comment.getByUser().getParseFile("profileImage") != null) {
                Glide.with(context)
                        .load(comment.getByUser().getParseFile("profileImage").getUrl())
                        .into(ivProfileImage);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_baseline_person_outline_24)
                        .into(ivProfileImage);
            }
        }
    }
}
