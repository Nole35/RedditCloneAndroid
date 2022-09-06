package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.OdgNaPost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OdgNaPostServis {

    @GET("api/reactionPost/post/{postId}")
    Call<Karma> getPostKarma(@Path("postId") int postId);

    @POST("api/reactionPost/create")
    Call<OdgNaPost> createReaction(@Body OdgNaPost odgNaPost);

    @GET("api/reactionPost/postByUser/{username}")
    Call<Karma> getPostKarmaByUser(@Path("username") String username);


}
