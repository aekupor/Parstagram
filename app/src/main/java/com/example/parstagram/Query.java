package com.example.parstagram;

import com.example.parstagram.models.Comment;
import com.example.parstagram.models.Post;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class Query {

    public static final String TAG = "Query";
    public static final String KEY_FOLLOWERS = "followers";

    public void queryUser(String userId, FindCallback<User> callback) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereMatches("objectId", userId);
        query.findInBackground(callback);
    }

    public void queryPosts(User user, FindCallback<Post> callback) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(callback);
    }

    public void findFollowers(User user, FindCallback<User> callback) {
        ParseQuery<User> query = ParseQuery.getQuery("_User");
        query.whereEqualTo(KEY_FOLLOWERS, user);
        query.findInBackground(callback);
    }

    public void queryPostsByPage(Integer page, FindCallback<Post> callback) {
        Integer displayLimit = 20;
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(displayLimit);
        query.setSkip(page * displayLimit);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(callback);
    }

    public void queryComments(String postId, FindCallback<Comment> callback) {
        Integer displayLimit = 20;

        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.setLimit(displayLimit);

        //since comparing a pointer, need to create pointer to compare to
        ParseObject obj = ParseObject.createWithoutData("Post", postId);
        query.whereEqualTo("forPost", obj);

        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.findInBackground(callback);
    }
}
