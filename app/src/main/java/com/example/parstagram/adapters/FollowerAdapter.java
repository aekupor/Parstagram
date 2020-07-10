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
import com.parse.ParseUser;

import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.ViewHolder> {

    public static final String TAG = "FollowAdapter";

    private Context context;
    private List<ParseUser> users;

    public FollowerAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_follower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUser user = users.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvUsername;
        ImageView ivProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsernameFollower);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImageFollower);

            itemView.setOnClickListener(this);
        }

        public void bind(ParseUser user) {
            tvUsername.setText(user.getUsername());
            ProfileImage getProfile = new ProfileImage();
            Glide.with(context)
                    .load(getProfile.getProfileImage(user))
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
                    ParseUser user = users.get(position);
                    Log.i(TAG, "user clicked: " + user.getUsername());

                    // go to OtherUserProfileFragment
                    final FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    Fragment fragment = OtherUserProfileFragment.newInstance(user.getObjectId());
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
            }
        }
    }
}
