package com.example.redditcloneandroid.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServis {

    private Retrofit retrofit;


    public static final String BASE_URL = "http://192.168.0.24:8080";

    public RetrofitServis(){
        initilazeRetrofit();
    }

    private void initilazeRetrofit() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

}
