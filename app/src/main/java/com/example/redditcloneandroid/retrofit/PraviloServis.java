package com.example.redditcloneandroid.retrofit;

import com.example.redditcloneandroid.model.Pravilo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PraviloServis {

    @GET("api/rules/community/{communityId}")
    Call<List<Pravilo>> getRulesByCommunity(@Path("communityId") int communityId);

    @POST("api/rules/create")
    Call<Pravilo> createRule(@Body Pravilo pravilo);

    @PUT("api/rules/{ruleId}")
    Call<Pravilo> updateRule(@Path("ruleId") int ruleId, @Body Pravilo pravilo);

    @DELETE("api/rules/{ruleId}")
    Call<ResponseBody> deleteRule(@Path("ruleId") int ruleId);

}
