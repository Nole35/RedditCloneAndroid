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

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.AzuriranjePosta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AzuriranjeKorPosF extends Fragment{

    private RecyclerView recyclerViewPosts;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_azuriranje_postova_korisnik, container, false);


        recyclerViewPosts = view.findViewById(R.id.recyclerViewUserPosts);
        recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getUserPosts();

                refreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getUserPosts();
    }


    public void getUserPosts(){

        RetrofitServis retrofitServis = new RetrofitServis();
        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);

        Korisnik korisnik = JWTUtils.getCurrentUser();

        Call<List<Post>> call = postServis.getOneUserPosts(korisnik.getKorIme());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                recyclerViewPosts.setAdapter(new AzuriranjePosta(response.body(), getContext()));
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Logger.getLogger(KorPostoviF.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

}