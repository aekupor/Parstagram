package com.example.parstagram.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_FOR_POST = "forPost";
    public static final String KEY_BY_USER = "byUser";
    public static final String KEY_TEXT = "commentText";

    // empty constructor needed by the Parceler library
    public Comment() { }

    public String getForPost() {
        return getString(KEY_FOR_POST);
    }

    public ParseUser getByUser() {
        return getParseUser(KEY_BY_USER);
    }

    public String getText() {
        return getString(KEY_TEXT);
    }

    public void setForPost(Post post) {
        put(KEY_FOR_POST, post);
    }

    public void setByUser(ParseUser user) {
        put(KEY_BY_USER, user);
    }

    public void setText(String text) {
        put(KEY_TEXT, text);
    }
}
