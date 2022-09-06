package com.example.redditcloneandroid.model;

public class Flair {

    private int idFlair;
    private String naziv;
    private int idZaj;
    private String aktivan;

    public Flair(int idFlair, String naziv, int idZaj, String aktivan) {
        this.idFlair = idFlair;
        this.naziv = naziv;
        this.idZaj = idZaj;
        this.aktivan = aktivan;
    }

    public Flair(){

    }

    public int getIdFlair() {
        return idFlair;
    }

    public void setIdFlair(int idFlair) {
        this.idFlair = idFlair;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getIdZaj() {
        return idZaj;
    }

    public void setIdZaj(int idZaj) {
        this.idZaj = idZaj;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }
}
