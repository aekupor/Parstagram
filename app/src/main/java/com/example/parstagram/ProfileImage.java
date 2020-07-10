package com.example.parstagram;

import com.example.parstagram.models.User;

import java.io.Serializable;

public class ProfileImage {

    public Serializable getProfileImage(User user) {
        if (user.getParseFile("profileImage") != null) {
            return user.getParseFile("profileImage").getUrl();
        } else {
            return R.drawable.ic_baseline_person_outline_24;
        }
    }
}
