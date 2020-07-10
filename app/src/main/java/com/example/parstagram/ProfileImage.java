package com.example.parstagram;

import com.parse.ParseUser;

import java.io.Serializable;

public class ProfileImage {

    public Serializable getProfileImage(ParseUser user) {
        if (user.getParseFile("profileImage") != null) {
            return user.getParseFile("profileImage").getUrl();
        } else {
            return R.drawable.ic_baseline_person_outline_24;
        }
    }
}
