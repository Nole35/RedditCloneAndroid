package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.OdgNaKom;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OdgNaKomServis {

    @GET("api/reactionComment/commentByUser/{username}")
    Call<Karma> getCommentKarmaByUser(@Path("username") String username);

    @GET("api/reactionComment/comment/{commentId}")
    Call<Karma> getCommentKarma(@Path("commentId") int commentId);

    @POST("api/reactionComment/create")
    Call<OdgNaKom> createReaction(@Body OdgNaKom odgNaKom);

}
