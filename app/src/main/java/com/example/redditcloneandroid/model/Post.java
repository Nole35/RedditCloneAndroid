package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Post {

    @SerializedName("postId")
    private int idPost;

    @SerializedName("title")
    private String naslov;

    @SerializedName("text")
    private String tekst;

    @SerializedName("creationDate")
    private String datKreiranja;

    @SerializedName("imagePath")
    private String imagePath;

    @SerializedName("community")
    private int zajednica;

    @SerializedName("user")
    private int korisnik;

    @SerializedName("flair")
    private int flair;

    @SerializedName("active")
    private String aktivan;


    public Post(int idPost, String naslov, String tekst, String datKreiranja, String imagePath, int flair, int zajednica, int korisnik, String aktivan) {
        this.idPost = idPost;
        this.naslov = naslov;
        this.tekst = tekst;
        this.datKreiranja = datKreiranja;
        this.imagePath = imagePath;
        this.zajednica = zajednica;
        this.flair = flair;
        this.korisnik = korisnik;
        this.aktivan = aktivan;
    }

    public Post(){

    }

    public int getFlair() {
        return flair;
    }

    public void setFlair(int flair) {
        this.flair = flair;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }



    public int getZajednica() {
        return zajednica;
    }

    public void setZajednica(int zajednica) {
        this.zajednica = zajednica;
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

    public String getDatKreiranja() {
        return datKreiranja;
    }

    public void setDatKreiranja(String datKreiranja) {
        this.datKreiranja = datKreiranja;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }
}





