package com.example.parstagram.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.ProfileImage;
import com.example.parstagram.models.Message;
import com.example.parstagram.R;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final String TAG = "ChatAdapter";

    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        //set profile image
        final ImageView profileView = isMe ? holder.imageMe : holder.imageOther;
        if (isMe) {
            ProfileImage getProfile = new ProfileImage();
            Glide.with(mContext)
                    .load(getProfile.getProfileImage((User) ParseUser.getCurrentUser()))
                    .into(profileView);
        } else {
            ParseQuery query = ParseUser.getQuery();
            query.whereEqualTo("objectId", message.getUserId());
            query.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        ProfileImage getProfile = new ProfileImage();
                        Glide.with(mContext)
                                .load(getProfile.getProfileImage((User) objects.get(0)))
                                .into(profileView);
                    } else {
                        Log.e(TAG, "error with finding user profile image");
                    }
                }
            });
        }
        holder.body.setText(message.getBody());
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOther;
        ImageView imageMe;
        TextView body;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = (ImageView)itemView.findViewById(R.id.ivProfileOther);
            imageMe = (ImageView)itemView.findViewById(R.id.ivProfileMe);
            body = (TextView)itemView.findViewById(R.id.tvBody);
        }
    }
}