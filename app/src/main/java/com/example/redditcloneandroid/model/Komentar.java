package com.example.redditcloneandroid.model;

import java.time.LocalDate;

public class Komentar {

    private int idKom;
    private int korisnik;
    private int post;
    private String tekst;
    private String timestamp;

    public Komentar(int idKom, String tekst, String timestamp, int korisnik, int post) {
        this.idKom = idKom;
        this.tekst = tekst;
        this.korisnik = korisnik;
        this.post = post;
        this.timestamp = timestamp;

    }

    public Komentar(){

    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(int korisnik) {
        this.korisnik = korisnik;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }



    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }
}
