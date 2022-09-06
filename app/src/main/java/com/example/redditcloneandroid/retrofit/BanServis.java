package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Ban;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BanServis {

    @POST("api/banned/create")
    Call<Ban> createBan(@Body Ban ban);
}
