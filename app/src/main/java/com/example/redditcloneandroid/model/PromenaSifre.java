package com.example.redditcloneandroid.model;

public class PromenaSifre {

    private String proslaSifra;
    private String novaSifra;

    public PromenaSifre(String proslaSifra, String novaSifra) {
        this.proslaSifra = proslaSifra;
        this.novaSifra = novaSifra;
    }

    public PromenaSifre(){

    }

    public String getProslaSifra() {
        return proslaSifra;
    }

    public void setProslaSifra(String proslaSifra) {
        this.proslaSifra = proslaSifra;
    }

    public String getNovaSifra() {
        return novaSifra;
    }

    public void setNovaSifra(String novaSifra) {
        this.novaSifra = novaSifra;
    }
}
