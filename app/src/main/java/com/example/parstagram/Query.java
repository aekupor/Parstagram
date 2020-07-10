package com.example.parstagram;

import com.example.parstagram.models.User;
import com.parse.FindCallback;
import com.parse.ParseQuery;

public class Query {

    public static final String TAG = "Query";

    //find callback as parameter
    public void queryUser(String userId, FindCallback<User> callback) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereMatches("objectId", userId);
        query.findInBackground(callback);
    }
}
