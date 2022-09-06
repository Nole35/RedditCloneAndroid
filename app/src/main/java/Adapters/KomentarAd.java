package Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Komentar;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.OdgNaKom;
import com.example.redditcloneandroid.model.enums.Rakcija;
import com.example.redditcloneandroid.retrofit.OdgNaKomServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomentarAd extends RecyclerView.Adapter<KomentarAd.MyViewHolder> {

    private List<Komentar> komentarList;
    Context context;

    public KomentarAd(List<Komentar> komentarList, Context context){
        this.context = context;
        this.komentarList = komentarList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.komentari_prozor, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Komentar komentar = komentarList.get(position);

        //SET USERNAME WHERE USER ID
        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);
        Call<Korisnik> call = korisnikServis.getUserById(komentar.getKorisnik());
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

        //SET COMMENT KARMA
        OdgNaKomServis odgNaKomServis = retrofitServis.getRetrofit().create(OdgNaKomServis.class);
        Call<Karma> call2 = odgNaKomServis.getCommentKarma(komentar.getIdKom());
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

        holder.likeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Korisnik logKorisnik = JWTUtils.getCurrentUser();
                Komentar selectedKomentar = komentarList.get(position);

                if (logKorisnik != null){
                    OdgNaKom odgNaKom = new OdgNaKom();
                    odgNaKom.setRakcija(Rakcija.LAJK);
                    odgNaKom.setIdKor(logKorisnik.getIdKor());
                    odgNaKom.setIdKom(selectedKomentar.getIdKom());

                    Call<OdgNaKom> likeReaction = odgNaKomServis.createReaction(odgNaKom);
                    likeReaction.enqueue(new Callback<OdgNaKom>() {
                        @Override
                        public void onResponse(Call<OdgNaKom> call, Response<OdgNaKom> response) {
                            Toast.makeText(view.getContext(), "Successful like comment!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OdgNaKom> call, Throwable t) {
                            Logger.getLogger(KomentarAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.dislikeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Korisnik logKorisnik = JWTUtils.getCurrentUser();
                Komentar selectedKomentar = komentarList.get(position);

                if (logKorisnik != null){
                    OdgNaKom odgNaKom = new OdgNaKom();
                    odgNaKom.setRakcija(Rakcija.DISLAJK);
                    odgNaKom.setIdKor(logKorisnik.getIdKor());
                    odgNaKom.setIdKom(selectedKomentar.getIdKom());

                    Call<OdgNaKom> dislikeReaction = odgNaKomServis.createReaction(odgNaKom);
                    dislikeReaction.enqueue(new Callback<OdgNaKom>() {
                        @Override
                        public void onResponse(Call<OdgNaKom> call, Response<OdgNaKom> response) {
                            Toast.makeText(view.getContext(), "Successful dislike comment!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<OdgNaKom> call, Throwable t) {
                            Logger.getLogger(KomentarAd.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });
                }else{
                    Toast.makeText(view.getContext(), "Please login!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        holder.commentText.setText(komentar.getTekst());
    }

    @Override
    public int getItemCount() {
        return komentarList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, commentText, karma;
        ImageView likeComment, dislikeComment;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernameComm);
            commentText = itemView.findViewById(R.id.textComment);
            karma = itemView.findViewById(R.id.commentKarma);
            likeComment = itemView.findViewById(R.id.likeCommentBtn);
            dislikeComment = itemView.findViewById(R.id.dislikeCommentBtn);
        }
    }
}
