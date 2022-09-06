package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.ZajedniceAd;
import Adapters.PostAd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    public RecyclerView recyclerViewPosts;
    public RecyclerView recyclerViewCommunity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        Button logIn = (Button)findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, LogInActivity.class);

                startActivity(intent);
            }
        });

        Button signUp = (Button)findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, RegistracijaA.class);

                startActivity(intent);
            }
        });



        recyclerViewPosts = (RecyclerView) findViewById(R.id.recyclerViewPostsForHome);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCommunity = (RecyclerView) findViewById(R.id.recyclerViewCommunity);
        recyclerViewCommunity.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onResume(){
        super.onResume();
        getAllPosts();
        getAllCommunity();
    }

    public void getAllPosts(){

        RetrofitServis retrofitServis = new RetrofitServis();
        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);

        Call<List<Post>> call = postServis.getAllPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerViewPosts.setAdapter(new PostAd(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Niste sacuvali post", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void getAllCommunity(){

        RetrofitServis retrofitServis = new RetrofitServis();
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);

        Call<List<Zajednica>> call = zajednicaServis.getAllCommunity();

        call.enqueue(new Callback<List<Zajednica>>() {
            @Override
            public void onResponse(Call<List<Zajednica>> call, Response<List<Zajednica>> response) {

                recyclerViewCommunity.setAdapter(new ZajedniceAd(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Zajednica>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Niste sacuvali zajednicu", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }




}