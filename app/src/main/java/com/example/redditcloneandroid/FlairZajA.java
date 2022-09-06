package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.Flair;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.FlairServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.FlairoviAda;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlairZajA extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addFlair;
    String communityName;
    private SwipeRefreshLayout refreshLayout;
    public Zajednica zajednica;

    public int communityId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flair_zajednica);

        getSupportActionBar().hide();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            communityName = extras.getString("communityName");
        }

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getCommunity();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerView = findViewById(R.id.recyclerViewFlairs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btn = findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlairZajA.super.onBackPressed();
            }
        });

        addFlair = findViewById(R.id.addFlairBtn);
        addFlair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.izmena_naslova_posta_prozor, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewFlairDescription = popupView.findViewById(R.id.newTitleForPost);
                inputNewFlairDescription.setHint("Flair name");

                AppCompatButton addFlair = popupView.findViewById(R.id.btnSubmitChangeTitle);
                addFlair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String flairName = inputNewFlairDescription.getText().toString();

                        if(inputNewFlairDescription.length() != 0){
                            Flair flair = new Flair();
                            flair.setIdZaj(communityId);
                            flair.setNaziv(flairName);

                            RetrofitServis retrofitServis = new RetrofitServis();
                            FlairServis flairServis = retrofitServis.getRetrofit().create(FlairServis.class);
                            Call<Flair> createFlair = flairServis.createFlair(flair);
                            createFlair.enqueue(new Callback<Flair>() {
                                @Override
                                public void onResponse(Call<Flair> call, Response<Flair> response) {
                                    Toast.makeText(FlairZajA.this, "Uspesno kreiran flair!", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Flair> call, Throwable t) {
                                    Toast.makeText(FlairZajA.this, "Neuspesno kreiran flair!", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(ZajednicaA.class.getName()).log(Level.SEVERE,"Error", t);
                                }
                            });
                        }else {
                            inputNewFlairDescription.setError("This field is required");
                        }




                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });

                    }
                });

            }
        });


    }


    public void onResume(){
        super.onResume();
        getCommunity();

    }

    private void getFlairs(int communityId, int moderator) {

        RetrofitServis retrofitServis = new RetrofitServis();
        FlairServis flairServis = retrofitServis.getRetrofit().create(FlairServis.class);

        Call<List<Flair>> getAllFlair = flairServis.getFlairsByCommunity(communityId);
        getAllFlair.enqueue(new Callback<List<Flair>>() {
            @Override
            public void onResponse(Call<List<Flair>> call, Response<List<Flair>> response) {
                recyclerView.setAdapter(new FlairoviAda(response.body(), getApplicationContext(), moderator));
            }

            @Override
            public void onFailure(Call<List<Flair>> call, Throwable t) {

            }
        });

    }

    private void getCommunity() {

        RetrofitServis retrofitServis = new RetrofitServis();
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);

        Call<Zajednica> call = zajednicaServis.getCommunityByName(communityName);
        call.enqueue(new Callback<Zajednica>() {
            @Override
            public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {

                communityId = response.body().getIdZaj();

                checkLoginUser(response.body().getModerator());
                getFlairs(response.body().getIdZaj(), response.body().getModerator());

            }

            @Override
            public void onFailure(Call<Zajednica> call, Throwable t) {
                Logger.getLogger(ZajednicaA.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void checkLoginUser(int moderator){

        Korisnik korisnik = JWTUtils.getCurrentUser();

        if(!(korisnik != null && korisnik.getIdKor() == moderator)){
            addFlair.setVisibility(View.GONE);
        }

    }

}