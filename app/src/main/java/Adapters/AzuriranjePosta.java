package Adapters;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redditcloneandroid.R;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.Post;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.PostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class AzuriranjePosta extends RecyclerView.Adapter<AzuriranjePosta.MyViewHolder>{

    private List<Post> postList;
    private Context context;

    private String username;
    private String communityName;

    public AzuriranjePosta(List<Post> postList, Context context) {

        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public AzuriranjePosta.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.azuriranje_posta_prozor, parent, false);


        return new AzuriranjePosta.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AzuriranjePosta.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Post post1 = postList.get(position);

        //SET USERNAME WHERE USER ID
        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);
        Call<Korisnik> call = korisnikServis.getUserById(post1.getKorisnik());
        call.enqueue(new Callback<Korisnik>() {
            @Override
            public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {
                holder.username.setText("@" + response.body().getKorIme());
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


        holder.postText.setText(post1.getTekst());
        holder.postTitle.setText(post1.getNaslov());

        //POPUP CHANGE TITLE
        holder.btnChangeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.izmena_naslova_posta_prozor, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewTitle = popupView.findViewById(R.id.newTitleForPost);

                AppCompatButton changeTitle = popupView.findViewById(R.id.btnSubmitChangeTitle);
                changeTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newTitle = inputNewTitle.getText().toString();

                        Post changedPost = postList.get(position);
                        changedPost.setNaslov(newTitle);

                        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);
                        Call<Post> changePost = postServis.changePost(changedPost, changedPost.getIdPost());
                        changePost.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                Toast.makeText(view.getContext(), "Successful change post title!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                        popupWindow.dismiss();

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

        //POPUP CHANGE TEXT
        holder.btnCahangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.izmena_posta_prozor, null);

                int width = 600;
                int height = 500;
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                EditText inputNewText = popupView.findViewById(R.id.newTextForPost);

                AppCompatButton changeText = popupView.findViewById(R.id.btnSubmitChangeText);
                changeText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String newText = inputNewText.getText().toString();

                        Post changedPost = postList.get(position);
                        changedPost.setTekst(newText);

                        PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);
                        Call<Post> changePost = postServis.changePost(changedPost, changedPost.getIdPost());
                        changePost.enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(Call<Post> call, Response<Post> response) {
                                Toast.makeText(view.getContext(), "Successful change post text!", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onFailure(Call<Post> call, Throwable t) {
                                Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                        popupWindow.dismiss();

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

        holder.btnDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Post selectedPost = postList.get(position);

                PostServis postServis = retrofitServis.getRetrofit().create(PostServis.class);
                Call<ResponseBody> deletePost = postServis.deleteItem(selectedPost.getIdPost());

                deletePost.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Toast.makeText(view.getContext(), "Successful delete post!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Logger.getLogger(PostAd.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });

            }
        });



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, community, postTitle, postText;
        AppCompatButton btnChangeTitle, btnCahangeText, btnDeletePost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            community = itemView.findViewById(R.id.zajednica);
            postTitle = itemView.findViewById(R.id.postTitle);
            postText = itemView.findViewById(R.id.postText);
            btnChangeTitle = itemView.findViewById(R.id.buttonChangeTitle);
            btnCahangeText = itemView.findViewById(R.id.buttonChangeTExt);
            btnDeletePost = itemView.findViewById(R.id.buttonDeletePost);

        }
    }

}
