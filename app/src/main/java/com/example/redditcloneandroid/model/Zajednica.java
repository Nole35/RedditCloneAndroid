package com.example.redditcloneandroid.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

public class Zajednica {

    @SerializedName("communityId")
    private int idZaj;

    @SerializedName("name")
    private String naziv;

    @SerializedName("description")
    private String oZaj;

    @SerializedName("creationDate")
    private String datKreiranjaZaj;

    @SerializedName("moderator")
    private int moderator;

    @SerializedName("active")
    private String aktivan;

    public Zajednica(int idZaj, String naziv, String oZaj, String datKreiranjaZaj, int moderator, String aktivan) {
        this.idZaj = idZaj;
        this.naziv = naziv;
        this.oZaj = oZaj;
        this.datKreiranjaZaj = datKreiranjaZaj;
        this.moderator = moderator;
        this.aktivan = aktivan;
    }

    public Zajednica(){

    }

    public int getIdZaj() {
        return idZaj;
    }

    public void setIdZaj(int idZaj) {
        this.idZaj = idZaj;
    }

    public String getNaziv() { return naziv; }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getoZaj() {
        return oZaj;
    }

    public void setoZaj(String oZaj) {
        this.oZaj = oZaj;
    }



    public String getAktivan() {
        return aktivan;
    }

    public void setAktivan(String aktivan) {
        this.aktivan = aktivan;
    }

    public String getDatKreiranjaZaj() {
        return datKreiranjaZaj;
    }

    public void setDatKreiranjaZaj(String datKreiranjaZaj) {
        this.datKreiranjaZaj = datKreiranjaZaj;
    }

    public int getModerator() {
        return moderator;
    }

    public void setModerator(int moderator) {
        this.moderator = moderator;
    }
}
