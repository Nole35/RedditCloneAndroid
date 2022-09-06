package com.example.redditcloneandroid.model;

import com.example.redditcloneandroid.model.enums.TipKorisnika;

public class Korisnik {

    private int idKor;
    private String korIme;
    private String sifra;
    private String email;
    private TipKorisnika tipKorisnika;
    private String avatar;
    private String datKreiranjaNaloga;
    private String oKor;
    private String korImeEkr;
    private String aktivan;

    public Korisnik(int idKor, String korIme, String sifra, String email, String avatar, String datKreiranjaNaloga, String oKor, String korImeEkr, TipKorisnika tipKorisnika, String aktivan) {
        this.idKor = idKor;
        this.korIme = korIme;
        this.sifra = sifra;
        this.email = email;
        this.tipKorisnika = tipKorisnika;
        this.korImeEkr = korImeEkr;
        this.avatar = avatar;
        this.datKreiranjaNaloga = datKreiranjaNaloga;
        this.oKor = oKor;
        this.aktivan = aktivan;
    }

    public Korisnik(){

    }

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public String getKorIme() {
        return korIme;
    }

    public void setKorIme(String korIme) {
        this.korIme = korIme;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public TipKorisnika getTipKorisnika() {
        return tipKorisnika;
    }

    public void setTipKorisnika(TipKorisnika tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDatKreiranjaNaloga() {
        return datKreiranjaNaloga;
    }

    public void setDatKreiranjaNaloga(String datKreiranjaNaloga) {
        this.datKreiranjaNaloga = datKreiranjaNaloga;
    }

    public String getoKor() {
        return oKor;
    }

    public void setoKor(String oKor) {
        this.oKor = oKor;
    }

    public String getKorImeEkr() {
        return korImeEkr;
    }

    public void setKorImeEkr(String korImeEkr) {
        this.korImeEkr = korImeEkr;
    }

    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }
}
