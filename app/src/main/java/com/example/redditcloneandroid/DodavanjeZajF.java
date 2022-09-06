package com.example.redditcloneandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.Zajednica;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.enums.TipKorisnika;
import com.example.redditcloneandroid.retrofit.ZajednicaServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DodavanjeZajF extends Fragment {

    EditText inputCommunityName;
    EditText inputDescription;
    AppCompatButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dodavanje_zaajednice, container, false);

        RetrofitServis retrofitServis = new RetrofitServis();

        inputCommunityName = view.findViewById(R.id.communityNameTxt);
        inputDescription = view.findViewById(R.id.descriptionTxt);
        btnSubmit = view.findViewById(R.id.btnAddCommunity);

        ZajednicaServis zajednicaServis = retrofitServis.getRetrofit().create(ZajednicaServis.class);
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String communityName = inputCommunityName.getText().toString();
                String description = inputDescription.getText().toString();

                Korisnik korisnik = JWTUtils.getCurrentUser();

                if (isValid()){
                    Zajednica zajednica = new Zajednica();
                    zajednica.setNaziv(communityName);
                    zajednica.setoZaj(description);
                    zajednica.setModerator(korisnik.getIdKor());

                    if (korisnik.getTipKorisnika() == TipKorisnika.KORISNIK){
                        korisnikServis.changeUserToModerator(korisnik.getIdKor()).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(getContext(), "Postali ste moderator", Toast.LENGTH_SHORT).show();
                                JWTUtils.logout();

                                zajednicaServis.createCommunity(zajednica).enqueue(new Callback<Zajednica>() {
                                    @Override
                                    public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {
                                        Toast.makeText(getContext(), "Uspesno dodata zajednica", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), HomeActivity.class);

                                        startActivity(intent);
                                        Toast.makeText(getContext(), "Uloguj se", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Zajednica> call, Throwable t) {
                                        Toast.makeText(getContext(), "Niste dodali zajednicu", Toast.LENGTH_SHORT).show();
                                        Logger.getLogger(RegistracijaA.class.getName()).log(Level.SEVERE,"Error", t);
                                    }
                                });



                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Toast.makeText(getContext(), "Promena korisnika nije uspela", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(RegistracijaA.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });
                    }else{

                        zajednicaServis.createCommunity(zajednica).enqueue(new Callback<Zajednica>() {
                            @Override
                            public void onResponse(Call<Zajednica> call, Response<Zajednica> response) {
                                Toast.makeText(getContext(), "Uspesno ste dodali zajednicu", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), KorisnikA.class);

                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<Zajednica> call, Throwable t) {
                                Toast.makeText(getContext(), "Neuspesno dodata zajednica", Toast.LENGTH_SHORT).show();
                                Logger.getLogger(RegistracijaA.class.getName()).log(Level.SEVERE,"Error", t);
                            }
                        });

                    }




                }

            }
        });

        return view;


    }

    public boolean isValid(){

        if (inputCommunityName.length() == 0) {
            inputCommunityName.setError("Obavezno polje");
            return false;
        }

        if (inputDescription.length() == 0) {
            inputDescription.setError("Obavezno polje");
            return false;
        }


        return true;
    }


}