package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Korisnik;
import com.example.redditcloneandroid.retrofit.RetrofitServis;
import com.example.redditcloneandroid.retrofit.KorisnikServis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistracijaA extends AppCompatActivity {

    EditText inputUsername;
    EditText inputEmail;
    EditText inputPassword;
    AppCompatButton btnSignUp;

    public List<Korisnik> allKorisniks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

        getSupportActionBar().hide();

        getInputValue();

        TextView logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistracijaA.this, LogInActivity.class);

                startActivity(intent);
            }
        });

    }

    private void getInputValue(){
        inputUsername = findViewById(R.id.usernameTxt);
        inputEmail = findViewById(R.id.emailTxt);
        inputPassword = findViewById(R.id.passwordTxt);
        btnSignUp = findViewById(R.id.btnSignUp);

        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

        btnSignUp.setOnClickListener(view -> {
            String username = inputUsername.getText().toString();
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            if (CheckAllFields()){
                Korisnik korisnik = new Korisnik();
                korisnik.setKorIme(username);
                korisnik.setEmail(email);
                korisnik.setKorImeEkr("");
                korisnik.setoKor("");
                korisnik.setSifra(password);

            korisnikServis.createUser(korisnik).enqueue(new Callback<Korisnik>() {
                @Override
                public void onResponse(Call<Korisnik> call, Response<Korisnik> response) {
                    Toast.makeText(RegistracijaA.this, "Uspesna registracija", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistracijaA.this, LogInActivity.class);

                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<Korisnik> call, Throwable t) {
                    Toast.makeText(RegistracijaA.this, "Neuspesna registracija", Toast.LENGTH_SHORT).show();
                    Logger.getLogger(RegistracijaA.class.getName()).log(Level.SEVERE,"Error", t);
                }
            });
            }





        });


    }


    private boolean CheckAllFields() {


        RetrofitServis retrofitServis = new RetrofitServis();
        KorisnikServis korisnikServis = retrofitServis.getRetrofit().create(KorisnikServis.class);

        Call<List<Korisnik>> call = korisnikServis.getAllUsers();

        call.enqueue(new Callback<List<Korisnik>>() {
            @Override
            public void onResponse(Call<List<Korisnik>> call, Response<List<Korisnik>> response) {

                List<Korisnik> korisnikFromBody = response.body();

                for (Korisnik korisnik : korisnikFromBody){
                    allKorisniks.add(korisnik);
                }

            }

            @Override
            public void onFailure(Call<List<Korisnik>> call, Throwable t) {
                Toast.makeText(RegistracijaA.this, "Load users faled!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(HomeActivity.class.getName()).log(Level.SEVERE,"Error", t);
            }
        });




        //USERNAME
        if (inputUsername.length() == 0) {
            inputUsername.setError("Obavezno polje");
            return false;
        }

        for (Korisnik korisnik : allKorisniks){
            if (korisnik.getKorIme().equals(inputUsername.getText().toString())){
                inputUsername.setError("Postoji korisnik sa korisnickim imenom");
                return false;
            }
        }

        //EMAIL
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(inputEmail.getText().toString());

        if (inputEmail.length() == 0) {
            inputEmail.setError("Obavezno polje");
            return false;
        }else if(!matcher.find()){
            inputEmail.setError("Email primera nn@gmail.com");
            return false;
        }

        Pattern VALID_PASSWORD_ADDRESS_REGEX = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = VALID_PASSWORD_ADDRESS_REGEX.matcher(inputPassword.getText().toString());

        //PASSWORD
        if (inputPassword.length() == 0) {
            inputPassword.setError("Obavezno polje");
            return false;
        }else if(!matcher1.find()){
            inputPassword.setError("Sifra mora imati 8 karaktera, veliko slovo i broj");
            return false;
        }

        return true;
    }


}