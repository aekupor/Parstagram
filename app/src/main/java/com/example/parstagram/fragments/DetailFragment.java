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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.parstagram.ProfileImage;
import com.example.parstagram.Query;
import com.example.parstagram.R;
import com.example.parstagram.adapters.CommentAdapter;
import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class DetailFragment extends Fragment implements ComposeCommentFragment.ComposeCommentDialogListener {

    public static final String TAG = "DetailFragment";

    private TextView tvUsername;
    private TextView tvCreatedAt;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvLikes;
    private Button btnLike;
    private Button btnComment;
    private ImageView ivProfileImage;
    private Boolean liked;
    private Query query;

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
        btnComment = view.findViewById(R.id.btnComment);
        rvComments = view.findViewById(R.id.rvComments);
        ivProfileImage = view.findViewById(R.id.ivProfileImageDetail);

        liked = false;
        query = new Query();

        allComments = new ArrayList<>();
        adapter = new CommentAdapter(getContext(), allComments);

        rvComments.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvComments.setLayoutManager(linearLayoutManager);

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "like button clicked");
                if (!liked) {
                    if (post.getLikes() == null) {
                        tvLikes.setText("1");
                        post.setLikes(1);
                    } else {
                        Integer likes = post.getLikes() + 1;
                        tvLikes.setText(likes.toString());
                        post.setLikes(likes);
                    }
                    liked = true;
                    btnLike.setBackgroundResource(R.drawable.ufi_heart_active);
                } else {
                    Integer likes = post.getLikes() - 1;
                    tvLikes.setText(likes.toString());
                    post.setLikes(likes);
                    liked = false;
                    btnLike.setBackgroundResource(R.drawable.ufi_heart);
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

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "comment button clicked");
                showEditDialog();
            }
        });

        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfile();
            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfile();
            }
        });

        queryPost();
    }

    private void goToProfile() {
        // go to Profile Fragment
        final FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        Fragment fragment = OtherUserProfileFragment.newInstance(post.getUser().getObjectId());
        fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
    }

    private void queryPost() {
        query.queryOnePost(postId, new FindCallback<Post>() {
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
                            .transform(new RoundedCornersTransformation(30, 10))
                            .into(ivImage);
                }

                ProfileImage getProfile = new ProfileImage();
                Glide.with(getContext())
                        .load(getProfile.getProfileImage(post.getUser()))
                        .circleCrop()
                        .into(ivProfileImage);

                queryComments();
            }
        });
    }

    protected void queryComments() {
        query.queryComments(postId, new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Comment comment : comments) {
                    Log.i(TAG, "Comment text: " + comment.getText());
                }
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        // This is called when the dialog is completed and the results have been passed
        Log.i(TAG, "wrote comment: " + inputText);

        Comment comment = new Comment();
        comment.setByUser(ParseUser.getCurrentUser());
        comment.setForPost(post);
        comment.setText(inputText);
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!");
                queryComments();
            }
        });
    }

    // Call this method to launch the edit dialog
    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        ComposeCommentFragment composeCommentFragment = (ComposeCommentFragment) ComposeCommentFragment.newInstance();
        // SETS the target fragment for use later when sending results
        composeCommentFragment.setTargetFragment(DetailFragment.this, 300);
        composeCommentFragment.show(fm, "fragment_compose_comment");
    }
}

