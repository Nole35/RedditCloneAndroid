package com.example.redditcloneandroid.retrofit;

import java.util.List;

import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.PromenaSifre;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface KorisnikServis {


    @GET("api/users")
    Call<List<Korisnik>> getAllUsers();

    @GET("api/users/{username}")
    Call<Korisnik> getUserByUsername(@Path("username") String username);

    @GET("api/users/byId/{userId}")
    Call<Korisnik> getUserById(@Path("userId") int userId);

    @POST("api/users/create")
    Call<Korisnik> createUser(@Body Korisnik korisnik);

    @PUT("api/users/{username}")
    Call<ResponseBody> changePassword(@Body PromenaSifre promenaSifre, @Path("username") String username);

    @PUT("api/users/changeData/{username}")
    Call<Korisnik> updateUser(@Body Korisnik korisnik, @Path("username") String username);

    @DELETE("api/users/changeUserToModerator/{id}")
    Call<ResponseBody> changeUserToModerator(@Path("id") int userId);


}
