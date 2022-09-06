package com.example.redditcloneandroid.retrofit;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServis {

    private Retrofit retrofit;

    //PREKO NETA SA TELEFONA http://172.20.10.7:8080
    //PREKO KUCNOG NETA http://192.168.0.172:8080
    //INTERNET U LAJKOVCU http://192.168.1.5:8080
    //NEMANJIN STAN http://192.168.0.30:8080
    public static final String BASE_URL = "http://192.168.0.30:8080";

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
