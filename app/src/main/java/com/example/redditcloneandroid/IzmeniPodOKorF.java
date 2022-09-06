package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Karma;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.OdgNaKomServis;
import com.example.redditcloneandroid.retrofit.OdgNaPostServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IzmeniPodOKorF extends Fragment {

    EditText userEmail, userDisplayName, userDescription;
    AppCompatButton submitBtn;
    Korisnik logKorisnik;
    TextView postKarma, commentKarma;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_izmeni_podatke_o_korisniku, container, false);

        logKorisnik = JWTUtils.getCurrentUser();

        userEmail = view.findViewById(R.id.userEmail);
        userDisplayName = view.findViewById(R.id.userDisplayName);
        userDescription = view.findViewById(R.id.userDescription);
        submitBtn = view.findViewById(R.id.btnChangeData);
        postKarma = view.findViewById(R.id.postKarma);
        commentKarma = view.findViewById(R.id.commentKarma);

        userEmail.setText(logKorisnik.getEmail());
        userEmail.setEnabled(false);
        userDisplayName.setText(logKorisnik.getKorImeEkr());
        userDisplayName.setEnabled(false);
        userDescription.setText(logKorisnik.getoKor());
        userDescription.setEnabled(false);
        submitBtn.setText("CHANGE");

        getUserKarma();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (submitBtn.getText().toString().equals("CHANGE")){

                    userEmail.setEnabled(true);
                    userDisplayName.setEnabled(true);
                    userDescription.setEnabled(true);
                    submitBtn.setText("SUBMIT");
                }else {

                    String newEmail = userEmail.getText().toString();
                    String newDisplayName = userDisplayName.getText().toString();
                    String newDescription = userDescription.getText().toString();

                    if (isValid()){

                        logKorisnik.setEmail(newEmail);
                        logKorisnik.setKorImeEkr(newDisplayName);
                        logKorisnik.setoKor(newDescription);

                        RetrofitServis retrofitServis = new RetrofitServis();
                        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

                        Call<Korisnik> call = korisnikServis.updateUser(logKorisnik, logKorisnik.getKorIme());

                        call.enqueue(new Callback<Korisnik>() {
                            @Override
                            public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {

                                Toast.makeText(getContext(), "Promenili ste podatke o korisnikku", Toast.LENGTH_SHORT).show();
                                userEmail.setEnabled(false);
                                userDisplayName.setEnabled(false);
                                userDescription.setEnabled(false);
                                submitBtn.setText("CHANGE");

                            }

                            @Override
                            public void onFailure(Call<Korisnik> call, Throwable t) {
                                Toast.makeText(getContext(), "Neuspesna izmena", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                    }

                }

            }
        });



        return view;
    }


    private boolean isValid(){

        //EMAIL
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(userEmail.getText().toString());

        if (userEmail.length() == 0) {
            userEmail.setError("Obavezno polje");
            return false;
        }else if(!matcher.find()){
            userEmail.setError("Pogresan email");
            return false;
        }

        //DISPLAY NAME
        if (userDisplayName.length() == 0) {
            userDisplayName.setError("Obavezno polje");
            return false;
        }

        //DESCRIPTION
        if (userDescription.length() == 0) {
            userDescription.setError("Obavezno polje");
            return false;
        }


        return true;
    }

    private void getUserKarma(){

        RetrofitServis retrofitServis = new RetrofitServis();
        OdgNaKomServis odgNaKomServis = retrofitServis.getRetrofit().create(OdgNaKomServis.class);
        OdgNaPostServis odgNaPostServis = retrofitServis.getRetrofit().create(OdgNaPostServis.class);

        Call<Karma> getPostKarma = odgNaPostServis.getPostKarmaByUser(logKorisnik.getKorIme());
        Call<Karma> getCommentKarma = odgNaKomServis.getCommentKarmaByUser(logKorisnik.getKorIme());

        getPostKarma.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {

                postKarma.setText(String.valueOf(response.body().getKarma()));

            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Toast.makeText(getContext(), "Failed load post karma!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });

        getCommentKarma.enqueue(new Callback<Karma>() {
            @Override
            public void onResponse(Call<Karma> call, Response<Karma> response) {
                commentKarma.setText(String.valueOf(response.body().getKarma()));
            }

            @Override
            public void onFailure(Call<Karma> call, Throwable t) {
                Toast.makeText(getContext(), "Failed load comment karma!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });




    }


}