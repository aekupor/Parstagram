package com.example.parstagram;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import org.w3c.dom.Text;

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
            tvUsername.setText(comment.getByUser().getUsername());
            tvDescription.setText(comment.getText());

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

        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position
                Comment comment = comments.get(position);

                Log.i(TAG, "post clicked: " + comment.getByUser().getUsername());
            }
        }
    }
}
