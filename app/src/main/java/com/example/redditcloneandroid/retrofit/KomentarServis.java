package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Komentar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface KomentarServis {

    @GET("api/comments/{postId}")
    Call<List<Komentar>> getCommentsByPost(@Path("postId") int postId);

    @POST("api/comments/create")
    Call<Komentar> createComment(@Body Komentar komentar);



}
