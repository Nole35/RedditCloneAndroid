package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostList {

    @SerializedName("results")
    private List<Post> postovi;

    public PostList(List<Post> posts) {
        this.postovi = postovi;
    }

    public PostList(){

    }

    public List<Post> getPostovi() {
        return postovi;
    }

    public void setPostovi(List<Post> postovi) {
        this.postovi = postovi;
    }
}
