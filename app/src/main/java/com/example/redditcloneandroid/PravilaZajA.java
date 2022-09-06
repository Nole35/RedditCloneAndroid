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

import com.example.redditcloneandroid.model.Pravilo;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.PraviloServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.PravilaAd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PravilaZajA extends AppCompatActivity {

    RecyclerView recyclerView;
    Button addRules;
    String communityName;
    private SwipeRefreshLayout refreshLayout;
    public Zajednica zajednica;

    public int communityId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pravila_zajednice);

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

        recyclerView = findViewById(R.id.recyclerViewRules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ImageView btn = findViewById(R.id.backButtonRules);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PravilaZajA.super.onBackPressed();
            }
        });

        addRules = findViewById(R.id.addRulesBtn);
        addRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.izmena_naslova_posta_prozor, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewRuleDescription = popupView.findViewById(R.id.newTitleForPost);
                inputNewRuleDescription.setHint("Rule description");

                AppCompatButton addRule = popupView.findViewById(R.id.btnSubmitChangeTitle);
                addRule.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String ruleDescription = inputNewRuleDescription.getText().toString();

                        if (inputNewRuleDescription.length() != 0){

                            Pravilo pravilo = new Pravilo();
                            pravilo.setZajednica(communityId);
                            pravilo.setoPravilu(ruleDescription);

                            RetrofitServis retrofitServis = new RetrofitServis();
                            PraviloServis praviloServis = retrofitServis.getRetrofit().create(PraviloServis.class);
                            Call<Pravilo> createRule = praviloServis.createRule(pravilo);
                            createRule.enqueue(new Callback<Pravilo>() {
                                @Override
                                public void onResponse(Call<Pravilo> call, Response<Pravilo> response) {
                                    Toast.makeText(PravilaZajA.this, "Kreirali ste paravilo", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Pravilo> call, Throwable t) {
                                    Toast.makeText(PravilaZajA.this, "Niste kreirali pravilo", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(ZajednicaA.class.getName()).log(Level.SEVERE,"Error", t);
                                }
                            });

                        }else{
                            inputNewRuleDescription.setError("Obaveztno polje");
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

    private void getRules(int communityId, int moderator) {

        RetrofitServis retrofitServis = new RetrofitServis();
        PraviloServis praviloServis = retrofitServis.getRetrofit().create(PraviloServis.class);

        Call<List<Pravilo>> getAllRules = praviloServis.getRulesByCommunity(communityId);
        getAllRules.enqueue(new Callback<List<Pravilo>>() {
            @Override
            public void onResponse(Call<List<Pravilo>> call, Response<List<Pravilo>> response) {
                recyclerView.setAdapter(new PravilaAd(response.body(), getApplicationContext(), moderator));
            }

            @Override
            public void onFailure(Call<List<Pravilo>> call, Throwable t) {

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
                getRules(response.body().getIdZaj(), response.body().getModerator());

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
            addRules.setVisibility(View.GONE);
        }

    }




}