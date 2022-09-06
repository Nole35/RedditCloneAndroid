package com.example.redditcloneandroid.model;

public class Autentifikacija {

    private String sifra;
    private String korIme;


    public Autentifikacija(String korIme, String sifra) {
        this.korIme = korIme;
        this.sifra = sifra;
    }

    public Autentifikacija(){

    }


    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) { this.sifra = sifra;


    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }
}
