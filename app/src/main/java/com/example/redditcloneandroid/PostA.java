package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Service;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Komentar;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.OdgNaPost;
import com.example.redditcloneandroid.model.enums.Rakcija;
import com.example.redditcloneandroid.retrofit.KomentarServis;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.OdgNaPostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Adapters.KomentarAd;
import Adapters.PostAd;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostA extends AppCompatActivity {

    private RecyclerView recyclerViewComments;
    private SwipeRefreshLayout refreshLayout;
    private AppCompatButton brnAddComment;
    ImageView likeBtn, dislikeBtn;
    private TextView username, community, postTitle, postText, karma;

    private int selectedPostId;
    private Post post;
    private Korisnik logKorisnik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        getSupportActionBar().hide();

        if (JWTUtils.getCurrentUser() != null){
            logKorisnik = JWTUtils.getCurrentUser();
        }

        username = findViewById(R.id.usernameOnePost);
        community = findViewById(R.id.communityOnePost);
        postTitle = findViewById(R.id.postTitleOnePost);
        postText = findViewById(R.id.postTextOnePost);
        karma = findViewById(R.id.karmaOnePost);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedPostId = extras.getInt("post");
        }

        ImageView btn = findViewById(R.id.backButtonOnePost);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostA.super.onBackPressed();
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getPost();
                getComments();
                addComment();
                postReaction();

                refreshLayout.setRefreshing(false);
            }
        });

        recyclerViewComments = (RecyclerView) findViewById(R.id.commentsRecycle);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));

        getPost();
        getComments();
        addComment();
        postReaction();


    }

    public void addComment(){

        brnAddComment =  findViewById(R.id.btnAddComment);
        brnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.dodavanje_komentara_prozor, null);

                int width = 700;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewComment = popupView.findViewById(R.id.textForComment);

                AppCompatButton submitAddComment = popupView.findViewById(R.id.btnSubmitAddComment);
                submitAddComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newCommentText = inputNewComment.getText().toString();

                        if (inputNewComment.length() == 0){
                            inputNewComment.setError("Obavezno polje");
                        }else {

                            Korisnik korisnik = JWTUtils.getCurrentUser();

                            Komentar komentar = new Komentar();
                            komentar.setTekst(newCommentText);
                            komentar.setPost(selectedPostId);
                            komentar.setKorisnik(korisnik.getIdKor());

                            RetrofitServis retrofitServis = new RetrofitServis();
                            KomentarServis komentarServis = retrofitServis.getRetrofit().create(KomentarServis.class);
                            Call<Komentar> create = komentarServis.createComment(komentar);

                            create.enqueue(new Callback<Komentar>() {
                                @Override
                                public void onResponse(Call<Komentar> call, Response<Komentar> response) {
                                    Toast.makeText(PostA.this, "Dodali ste komentar", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }

                                @Override
                                public void onFailure(Call<Komentar> call, Throwable t) {
                                    Toast.makeText(PostA.this, "Niste dodali komentar", Toast.LENGTH_SHORT).show();
                                    Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                                }
                            });


                        }

                    }
                });

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


    public void getPost(){

        RetrofitServis retrofitServis = new RetrofitServis();
        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);
        Call<Post> call = postServis.getPostByID(selectedPostId);

        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);
        OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                post = response.body();

                Call<Korisnik> getUserUsername = korisnikServis.getUserById(post.getKorisnik());
                getUserUsername.enqueue(new Callback<Korisnik>() {
                    @Override
                    public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {
                        username.setText("@" + response.body().getKorIme());
                    }

                    @Override
                    public void onFailure(Call<Korisnik> call, Throwable t) {

                    }
                });

                Call<Zajednica> getCommunityName = zajednicaServis.getCommunityById(post.getZajednica());
                getCommunityName.enqueue(new Callback<Zajednica>() {
                    @Override
                    public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {
                        community.setText("@" + response.body().getNaziv());
                    }

                    @Override
                    public void onFailure(Call<Zajednica> call, Throwable t) {

                    }
                });

                Call<Karma> getPostKarma = odgNaPostServis.getPostKarma(post.getIdPost());
                getPostKarma.enqueue(new Callback<Karma>() {
                    @Override
                    public void onResponse(Call<Karma> call, Response<Karma> response) {
                        karma.setText(String.valueOf(response.body().getKarma()));
                    }

                    @Override
                    public void onFailure(Call<Karma> call, Throwable t) {
                        Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });

                postTitle.setText(post.getNaslov());
                postText.setText(post.getTekst());

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

    public void getComments(){

        RetrofitServis retrofitServis = new RetrofitServis();
        KomentarServis komentarServis = retrofitServis.getRetrofit().create(KomentarServis.class);
        Call<List<Komentar>> getPostComments = komentarServis.getCommentsByPost(selectedPostId);

        getPostComments.enqueue(new Callback<List<Komentar>>() {
            @Override
            public void onResponse(Call<List<Komentar>> call, Response<List<Komentar>> response) {
                recyclerViewComments.setAdapter(new KomentarAd(response.body(), getApplicationContext()));
            }

            @Override
            public void onFailure(Call<List<Komentar>> call, Throwable t) {
                Toast.makeText(PostA.this, "Failed to load comments!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

    }

    public void postReaction(){

        RetrofitServis retrofitServis = new RetrofitServis();
        likeBtn = findViewById(R.id.likeImgBtn);
        dislikeBtn = findViewById(R.id.dislikeImgBtn);

        //CREATE LIKE REACTION
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logKorisnik != null){
                    OdgNaPost odgNaPost = new OdgNaPost();
                    odgNaPost.setRakcija(Rakcija.LAJK);
                    odgNaPost.setIdKorP(logKorisnik.getIdKor());
                    odgNaPost.setIdPost(selectedPostId);

                    OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);
                    Call<OdgNaPost> likeReaction = odgNaPostServis.createReaction(odgNaPost);
                    likeReaction.enqueue(new Callback<OdgNaPost>() {
                        @Override
                        public void onResponse(Call<OdgNaPost> call, Response<OdgNaPost> response) {
                            Toast.makeText(view.getContext(), "Uspesna reakcija", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OdgNaPost> call, Throwable t) {
                            Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else {
                    Toast.makeText(view.getContext(), "Ulogujte se", Toast.LENGTH_SHORT).show();
                }



            }
        });



        //CREATE DISLIKE REACTION
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (logKorisnik != null){
                    OdgNaPost odgNaPost = new OdgNaPost();
                    odgNaPost.setRakcija(Rakcija.DISLAJK);
                    odgNaPost.setIdKorP(logKorisnik.getIdKor());
                    odgNaPost.setIdPost(selectedPostId);

                    OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);
                    Call<OdgNaPost> dislikeReaction = odgNaPostServis.createReaction(odgNaPost);
                    dislikeReaction.enqueue(new Callback<OdgNaPost>() {
                        @Override
                        public void onResponse(Call<OdgNaPost> call, Response<OdgNaPost> response) {
                            Toast.makeText(view.getContext(), "Uspesna reakcija", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<OdgNaPost> call, Throwable t) {
                            Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Ulogujte se!", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

}