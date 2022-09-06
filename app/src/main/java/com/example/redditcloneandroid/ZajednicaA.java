package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.PostAd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZajednicaA extends AppCompatActivity {

    private RecyclerView recyclerViewPosts;
    private String communityName;
    private SwipeRefreshLayout refreshLayout;
    private AppCompatButton addPost;

    public Zajednica zajednica = new Zajednica();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zajednica);

        addPost = findViewById(R.id.btnAddPost);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityName = extras.getString("communityName");

        }

        Korisnik logKorisnik = JWTUtils.getCurrentUser();
        if(logKorisnik == null){
            addPost.setVisibility(View.GONE);
        }

        getSupportActionBar().hide();

        ImageView btn = findViewById(R.id.backImgCommunity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZajednicaA.super.onBackPressed();
            }
        });

        Button rules = findViewById(R.id.btnRules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZajednicaA.this, PravilaZajA.class);
                intent.putExtra("communityName", communityName);
                startActivity(intent);
            }
        });

        AppCompatButton btnFlairs = findViewById(R.id.btnFlairs);
        btnFlairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ZajednicaA.this, FlairZajA.class);
                intent.putExtra("communityName", communityName);
                startActivity(intent);
            }
        });



        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logKorisnik == null){
                    Toast.makeText(view.getContext(), "Ulohujte se", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(ZajednicaA.this, DodavanjePostaA.class);
                    intent.putExtra("communityId", zajednica.getIdZaj());
                    startActivity(intent);
                }


            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllPosts();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerViewPosts = findViewById(R.id.recyclerViewPostsCommunity);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(this));

    }


    public void onResume(){
        super.onResume();
        getCommunity();
        getAllPosts();
    }

    public void getCommunity(){

        RetrofitServis retrofitServis = new RetrofitServis();
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);

        Call<Zajednica> call = zajednicaServis.getCommunityByName(communityName);
        call.enqueue(new Callback<Zajednica>() {
            @Override
            public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {

                zajednica = response.body();
            }

            @Override
            public void onFailure(Call<Zajednica> call, Throwable t) {
                Logger.getLogger(ZajednicaA.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }

    public void getAllPosts(){

        RetrofitServis retrofitServis = new RetrofitServis();
        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);

        Call<List<Post>> call = postServis.getPostsById(zajednica.getIdZaj());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerViewPosts.setAdapter(new PostAd(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(ZajednicaA.this, "Load community posts faled!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(ZajednicaA.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }




}