package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.UkiniZajednicu;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UkiniZajF extends Fragment {

    private RecyclerView recycleView;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ukini_zajednicu, container, false);


        recycleView = view.findViewById(R.id.suspendCommunityCardRecycler);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllCommunity();

                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getAllCommunity();
    }

    public void getAllCommunity(){

        RetrofitServis retrofitServis = new RetrofitServis();
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);

        Call<List<Zajednica>> call = zajednicaServis.getAllCommunity();

        call.enqueue(new Callback<List<Zajednica>>() {
            @Override
            public void onResponse(Call<List<Zajednica>> call, Response<List<Zajednica>> response) {

                recycleView.setAdapter(new UkiniZajednicu(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Zajednica>> call, Throwable t) {
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }

}