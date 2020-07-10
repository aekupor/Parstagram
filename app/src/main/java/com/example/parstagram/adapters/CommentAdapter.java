package com.example.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.ProfileImage;
import com.example.parstagram.R;
import com.example.parstagram.fragments.OtherUserProfileFragment;
import com.example.parstagram.models.Comment;
import com.parse.ParseException;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    public static final String TAG = "CommentAdapter";

    private Context context;
    private List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUsername;
        TextView tvDescription;
        ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsernameComment);
            tvDescription = itemView.findViewById(R.id.tvTextComment);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImageComment);

            itemView.setOnClickListener(this);
        }

        public void bind(Comment comment) {
            try {
                tvUsername.setText(comment.getByUser().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            }

            tvDescription.setText(comment.getText());

            ProfileImage getProfile = new ProfileImage();
            Glide.with(context)
                    .load(getProfile.getProfileImage(comment.getByUser()))
                    .into(ivProfileImage);
        }

        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // make sure the position is valid, i.e. actually exists in the view
                if (position != RecyclerView.NO_POSITION) {
                    // get the post at the position
                    Comment comment = comments.get(position);
                    Log.i(TAG, "comment clicked: " + comment.getByUser().getUsername());

                    // go to Profile Fragment
                    final FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    Fragment fragment = OtherUserProfileFragment.newInstance(comment.getByUser().getObjectId());
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            }
        }
    }
}
