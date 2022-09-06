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

import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.model.PromenaSifre;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromeniSifruF extends Fragment {

    EditText inputCurrentPassword;
    EditText inputNewPassword;
    AppCompatButton btnSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_izmeni_sifru, container, false);

        inputCurrentPassword = view.findViewById(R.id.currentPasswordTxt);
        inputNewPassword = view.findViewById(R.id.newPasswordTxt);
        btnSubmit = view.findViewById(R.id.btnSubmitChangePassword);

        Korisnik korisnik = JWTUtils.getCurrentUser();
        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentPassword = inputCurrentPassword.getText().toString();
                String newPassword = inputNewPassword.getText().toString();

                if (isValid()){

                    PromenaSifre promenaSifre = new PromenaSifre();
                    promenaSifre.setProslaSifra(currentPassword);
                    promenaSifre.setNovaSifra(newPassword);

                    Call<ResponseBody> call = korisnikServis.changePassword(promenaSifre, korisnik.getKorIme());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (response.code() == 200){
                                Toast.makeText(getContext(), "Promenili ste sifru", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), KorisnikA.class);
                                startActivity(intent);
                            }else {
                                inputCurrentPassword.setText("");
                                inputNewPassword.setText("");
                                Toast.makeText(getContext(), "Niste promenili sifru", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Niste promenili sifru", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(RegistracijaA.class.getName()).log(Level.SEVERE,"Error", t);
                        }
                    });

                }



            }
        });



        return view;
    }

    private boolean isValid(){

        if (inputCurrentPassword.length() == 0){
            inputCurrentPassword.setError("Obaveznopolje");
        }

        Pattern VALID_PASSWORD_ADDRESS_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = VALID_PASSWORD_ADDRESS_REGEX.matcher(inputNewPassword.getText().toString());

        if (inputNewPassword.length() == 0) {
            inputNewPassword.setError("Obavezno polje");
            return false;
        }else if(!matcher1.find()){
            inputNewPassword.setError("Sifra mora imati  veliko slovo, broj i 8 karaktera");
            return false;
        }

        return true;
    }

}