package com.example.parstagram;

import com.example.parstagram.models.Post;
import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseQuery;

public class Query {

    public static final String TAG = "Query";

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
}
