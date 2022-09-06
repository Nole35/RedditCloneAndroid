package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.ZajednicaA;
import com.example.redditcloneandroid.PostA;
import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.model.OdgNaPost;
import com.example.redditcloneandroid.model.enums.Rakcija;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.OdgNaPostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAd extends RecyclerView.Adapter<PostAd.MyViewHolder>{

    private List<Post> postList;
    private Context context;

    private String username;
    private String communityName;

    public PostAd(List<Post> postList, Context context) {

        this.postList = postList;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_prozor, parent, false);



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Post post1 = postList.get(position);

        holder.commImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);

                Intent intent= new Intent(context, PostA.class);
                intent.putExtra("post", selectedPost.getIdPost());
                context.startActivity(intent);
            }
        });

        holder.commTxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post selectedPost = postList.get(position);

                Intent intent= new Intent(context, PostA.class);
                intent.putExtra("post", selectedPost.getIdPost());
                context.startActivity(intent);
            }
        });


        //SET USERNAME WHERE USER ID
        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);
        Call<Korisnik> call = korisnikServis.getUserById(post1.getKorisnik());
        call.enqueue(new Callback<Korisnik>() {
            @Override
            public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {

                if (!response.body().getKorImeEkr().equals("")){
                    holder.username.setText("@" + response.body().getKorImeEkr());
                }else {
                    holder.username.setText("@" + response.body().getKorIme());
                }


            }

            @Override
            public void onFailure(Call<Korisnik> call, Throwable t) {
                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //SET COMMUNITY NAME WHERE COMMUNITY ID
        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);
        Call<Zajednica> call1 = zajednicaServis.getCommunityById(post1.getZajednica());
        call1.enqueue(new Callback<Zajednica>() {
            @Override
            public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {
                holder.community.setText("@" + response.body().getNaziv());
            }

            @Override
            public void onFailure(Call<Zajednica> call, Throwable t) {
                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //CLICK ON COMMUNITY NAME
        holder.community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityName1 = holder.community.getText().toString();
                System.out.println("Community " + communityName1.substring(1));
                Intent intent= new Intent(context, ZajednicaA.class);
                intent.putExtra("communityName", communityName1.substring(1));
                context.startActivity(intent);
            }
        });


        //SET POST KARMA
        OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);
        Call<Karma> call2 = odgNaPostServis.getPostKarma(post1.getIdPost());
        call2.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {
                holder.karma.setText(String.valueOf(response.body().getKarma()));
            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        //CREATE LIKE REACTION
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);
                Korisnik logKorisnik = JWTUtils.getCurrentUser();

                if (logKorisnik != null){
                    OdgNaPost odgNaPost = new OdgNaPost();
                    odgNaPost.setRakcija(Rakcija.LAJK);
                    odgNaPost.setIdKorP(logKorisnik.getIdKor());
                    odgNaPost.setIdPost(selectedPost.getIdPost());

                    OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);
                    Call<OdgNaPost> likeReaction = odgNaPostServis.createReaction(odgNaPost);
                    likeReaction.enqueue(new Callback<OdgNaPost>() {
                        @Override
                        public void onResponse(Call<OdgNaPost> call, Response<OdgNaPost> response) {
                            Toast.makeText(view.getContext(), "Successful like post!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OdgNaPost> call, Throwable t) {
                            Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else {
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }



            }
        });



        //CREATE DISLIKE REACTION
        holder.dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);
                Korisnik logKorisnik = JWTUtils.getCurrentUser();

                if (logKorisnik != null){
                    OdgNaPost odgNaPost = new OdgNaPost();
                    odgNaPost.setRakcija(Rakcija.DISLAJK);
                    odgNaPost.setIdKorP(logKorisnik.getIdKor());
                    odgNaPost.setIdPost(selectedPost.getIdPost());

                    OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);
                    Call<OdgNaPost> likeReaction = odgNaPostServis.createReaction(odgNaPost);
                    likeReaction.enqueue(new Callback<OdgNaPost>() {
                        @Override
                        public void onResponse(Call<OdgNaPost> call, Response<OdgNaPost> response) {
                            Toast.makeText(view.getContext(), "Successful dislike post!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<OdgNaPost> call, Throwable t) {
                            Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }



            }
        });


        holder.postText.setText(post1.getTekst());
        holder.postTitle.setText(post1.getNaslov());



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, community, postTitle, postText, karma, commTxtBtn;
        ImageView likeBtn, dislikeBtn, commImgBtn;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            community = itemView.findViewById(R.id.zajednica);
            postTitle = itemView.findViewById(R.id.postTitle);
            postText = itemView.findViewById(R.id.postText);
            karma = itemView.findViewById(R.id.karma);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            dislikeBtn = itemView.findViewById(R.id.dislikeBtn);
            commImgBtn = itemView.findViewById(R.id.commentsImgButton);
            commTxtBtn = itemView.findViewById(R.id.commentsTextButton);
        }
    }



}
