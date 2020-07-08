package com.example.parstagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.Comment;
import com.example.parstagram.CommentAdapter;
import com.example.parstagram.Post;
import com.example.parstagram.PostsAdapter;
import com.example.parstagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    public static final String TAG = "DetailFragment";

    private TextView tvUsername;
    private TextView tvCreatedAt;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvLikes;
    private Button btnLike;
    private RecyclerView rvComments;
    private CommentAdapter adapter;
    private List<Comment> allComments;

    private String postId;
    private Post post;

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
        tvLikes = view.findViewById(R.id.tvLikes);
        btnLike = view.findViewById(R.id.btnLike);
        rvComments = view.findViewById(R.id.rvComments);

        allComments = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), allComments);

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "like button clicked");
                if (post.getLikes() == null) {
                    tvLikes.setText("1");
                    post.setLikes(1);
                } else {
                    Integer likes = post.getLikes() + 1;
                    tvLikes.setText(likes.toString());
                    post.setLikes(likes);
                }
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                            Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Post save was successful!");
                    }
                });
            }
        });

        queryPost();
    }

    private void queryPost() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(1);
        query.whereEqualTo("objectId", postId);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                post = posts.get(0);
                Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());

                tvUsername.setText(post.getUser().getUsername());
                tvDescription.setText(post.getDescription());
                tvCreatedAt.setText(post.getCreatedAt().toString());
                if (post.getLikes() == null) {
                    tvLikes.setText("0");
                } else {
                    tvLikes.setText(post.getLikes().toString());
                }

                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(getContext())
                            .load(post.getImage().getUrl())
                            .into(ivImage);
                }
            }
        });
    }
}
