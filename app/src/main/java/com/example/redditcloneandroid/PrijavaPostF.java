package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import Adapters.OdgNaPostAd;

public class PrijavaPostF extends Fragment {

    RecyclerView recyclerView;
    String usernamePost[], communityPost[], titlePost[], textPost[], sortPosts[];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prijava_posta, container, false);

        recyclerView = view.findViewById(R.id.reportedPostRecycle);

        usernamePost = getResources().getStringArray(R.array.username_post);
        communityPost = getResources().getStringArray(R.array.community_post);
        titlePost = getResources().getStringArray(R.array.title_post);
        textPost = getResources().getStringArray(R.array.text_post);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        OdgNaPostAd postAdapter = new OdgNaPostAd(requireActivity(), usernamePost, communityPost, titlePost, textPost);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

    }

}