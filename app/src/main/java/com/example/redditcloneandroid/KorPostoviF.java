package com.example.redditcloneandroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class KorPostoviF extends Fragment {

    private RecyclerView recyclerViewPosts;
    private RecyclerView recyclerViewCommunity;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_korisnika_pocetna, container, false);

        recyclerViewPosts = view.findViewById(R.id.postUserPage);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewCommunity = view.findViewById(R.id.recyclerViewCommunityList);
        recyclerViewCommunity.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getAllPosts();
                getAllCommunity();

                refreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                recyclerViewPosts.setAdapter(new PostAd(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Logger.getLogger(KorPostoviF.class.getName()).log(Level.SEVERE,"Error", t);
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

                recyclerViewCommunity.setAdapter(new ZajedniceAd(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Zajednica>> call, Throwable t) {
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });


    }


}
