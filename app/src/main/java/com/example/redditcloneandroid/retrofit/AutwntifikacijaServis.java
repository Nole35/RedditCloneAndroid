package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Autentifikacija;
import com.example.redditcloneandroid.model.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AutwntifikacijaServis {

    @POST("api/users/login")
    Call<Token> login(@Body Autentifikacija request);

}
