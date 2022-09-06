package com.example.redditcloneandroid.model;

public class FlairZajednica {

    private int idFZaj;
    private String naziv;
    private Zajednica zajednice;
    private String aktivan;


    public FlairZajednica(int idFZaj, String naziv, Zajednica zajednice , String aktivan) {
        this.idFZaj = idFZaj;
        this.naziv = naziv;
        this.zajednice = zajednice;
        this.aktivan = aktivan;
    }

    public FlairZajednica() {

    }

    public int getIdFZaj() { return idFZaj; }

    public void setIdFZaj(int idFZaj) {
        this.idFZaj = idFZaj;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public Zajednica getZajednice() {
        return zajednice;
    }

    public void setZajednice(Zajednica zajednice) {
        this.zajednice = zajednice;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }
}
