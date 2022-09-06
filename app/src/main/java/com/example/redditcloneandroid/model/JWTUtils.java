package com.example.redditcloneandroid.model;

import android.util.Base64;
import android.util.Log;

import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JWTUtils {

    public static String currentUsername = "";
    public static String currentRoles = "";
    public static Korisnik currentKorisnik;


    public static void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));


            JSONObject object = new JSONObject(getJson(split[1]));

            currentUsername = object.getString("sub");
            currentRoles = object.getJSONObject("role").getString("authority");

        } catch (UnsupportedEncodingException e) {
            //Error
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static String getCurrentUserUsername(){
        return currentUsername;
    }

    public static String getCurrentUserRoles(){
        return currentRoles;
    }

    public static Korisnik getCurrentUser(){

        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

        if (currentUsername != ""){
            Call<Korisnik> call = korisnikServis.getUserByUsername(currentUsername);

            call.enqueue(new Callback<Korisnik>() {
                @Override
                public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {
                    currentKorisnik = response.body();
                }

                @Override
                public void onFailure(Call<Korisnik> call, Throwable t) {
                    //error
                }
            });

            return currentKorisnik;

        }else{
            return null;
        }


    }

    public static void logout(){
        currentUsername = "";
        currentRoles = "";
        currentKorisnik = null;
    }






}
