package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Zajednica;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ZajednicaServis {

    @GET("api/communities/{communityId}")
    Call<Zajednica> getCommunityById(@Path("communityId") int id);

    @GET("api/communities/byCommunityName/{communityName}")
    Call<Zajednica> getCommunityByName(@Path("communityName") String communityName);

    @GET("api/communities")
    Call<List<Zajednica>> getAllCommunity();

    @POST("api/communities/create")
    Call<Zajednica> createCommunity(@Body Zajednica zajednica);

    @DELETE("api/communities/{communityId}")
    Call<ResponseBody> deleteCommunity(@Path("communityId") int communityId);
}
