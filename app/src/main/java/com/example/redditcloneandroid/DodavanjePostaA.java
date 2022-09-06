package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DodavanjePostaA extends AppCompatActivity {

    EditText inputTitle;
    EditText inputDescription;
    AppCompatButton btnSubmit;

    int communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodavanje_posta);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityId = extras.getInt("communityId");

        }

        System.out.println("Community id " + communityId);

        RetrofitServis retrofitServis = new RetrofitServis();

        inputTitle = findViewById(R.id.titleTxt);
        inputDescription = findViewById(R.id.descriptionTxt);
        btnSubmit = findViewById(R.id.btnAddPost);

        ImageView btn = findViewById(R.id.backImgCommunity);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DodavanjePostaA.super.onBackPressed();
            }
        });

        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = inputTitle.getText().toString();
                String picture = "";
                String description = inputDescription.getText().toString();

                Korisnik korisnik = JWTUtils.getCurrentUser();

                if (isValid()){
                    Post post = new Post();
                    post.setTekst(description);
                    post.setNaslov(title);
                    post.setImagePath(picture);
                    post.setZajednica(communityId);
                    post.setFlair(1);
                    post.setKorisnik(korisnik.getIdKor());

                    postServis.createPost(post).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            Toast.makeText(DodavanjePostaA.this, "Dodali ste post!", Toast.LENGTH_SHORT).show();
                            DodavanjePostaA.super.onBackPressed();
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {
                            Toast.makeText(DodavanjePostaA.this, "Niste dodali post!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(DodavanjePostaA.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });

                }




            }
        });

    }


    public boolean isValid(){

        if (inputTitle.length() == 0) {
            inputTitle.setError("Obavezno polje");
            return false;
        }

        if (inputDescription.length() == 0) {
            inputDescription.setError("Obavezno polje");
            return false;
        }


        return true;
    }






}