package com.example.redditcloneandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.redditcloneandroid.model.Autentifikacija;
import com.example.redditcloneandroid.model.JWTUtils;
import com.example.redditcloneandroid.model.Token;
import com.example.redditcloneandroid.retrofit.AutwntifikacijaServis;
import com.example.redditcloneandroid.retrofit.RetrofitServis;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    public String userType;
    EditText inputUsername;
    EditText inputPassword;
    AppCompatButton btnLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        getInputValue();

        TextView signUp1 = findViewById(R.id.signUp);
        signUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, RegistracijaA.class);

                startActivity(intent);
            }
        });

    }

    private void getInputValue(){

        inputUsername = findViewById(R.id.usernameTxt);
        inputPassword = findViewById(R.id.passwordTxt);
        btnLogIn = findViewById(R.id.logIn);


        RetrofitServis retrofitServis = new RetrofitServis();
        AutwntifikacijaServis autwntifikacijaServis = retrofitServis.getRetrofit().create(AutwntifikacijaServis.class);

        btnLogIn.setOnClickListener(view -> {

            String username = inputUsername.getText().toString();
            String password = inputPassword.getText().toString();

            if (checkInputValue()){
                Autentifikacija request = new Autentifikacija();
                request.setKorIme(username);
                request.setSifra(password);

                autwntifikacijaServis.login(request).enqueue(new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {


                        if (response.body() != null){
                            String token = response.body().getAccessToken();

                            try {
                                JWTUtils.decoded(token);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(LogInActivity.this, "Ulogovali ste se", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, KorisnikA.class);

                            startActivity(intent);

                        }else {
                            Toast.makeText(LogInActivity.this, "Pogresna lozinka ili mejl", Toast.LENGTH_LONG).show();
                            inputPassword.setText("");
                            inputUsername.setText("");
                        }




                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Logger.getLogger(LogInActivity.class.getName()).log(Level.SEVERE,"Error", t);
                    }
                });


            }





        });

    }


    private boolean checkInputValue(){

        if (inputUsername.length() == 0) {
            inputUsername.setError("Obavezno polje");
            return false;
        }

        if (inputPassword.length() == 0) {
            inputPassword.setError("Obavezno polje");
            return false;
        }


        return true;
    }











}