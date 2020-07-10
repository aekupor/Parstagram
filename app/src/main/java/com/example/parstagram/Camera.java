package com.example.parstagram;

import com.example.parstagram.models.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class Camera {

    public static final String TAG = "CAMERA";

    private File photoFile;
    public String photoFileName = "photo.jpg";



    public void savePost(String description, ParseUser currentUser, File photoFile, SaveCallback callback) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(callback);
    }
}
