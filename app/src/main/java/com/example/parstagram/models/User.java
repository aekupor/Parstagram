package com.example.parstagram.models;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

public class User extends ParseUser {

    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_FOLLOWING = "following";

    public ArrayList<ParseUser> getFollowers() {
        return (ArrayList<ParseUser>) get(KEY_FOLLOWERS);
    }

    public void addFollower(ParseUser user) {
        put(KEY_FOLLOWERS, user);
    }

    public ArrayList<ParseUser> getFollowing() {
        return (ArrayList<ParseUser>) get(KEY_FOLLOWING);
    }

    public void addFollowing(ParseUser user) {
        ArrayList<ParseUser> following = getFollowers();
        if (following == null) {
            following = new ArrayList<>();
        }
        following.add(user);
        put(KEY_FOLLOWING, following);
    }

}
