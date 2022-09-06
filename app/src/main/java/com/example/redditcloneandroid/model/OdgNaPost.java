package com.example.redditcloneandroid.model;

import com.example.redditcloneandroid.model.enums.Rakcija;

public class OdgNaPost {

    private int idOdgP;
    private Rakcija rakcija;
    private String timestamp;
    private int idKorP;
    private int idPost;

    public OdgNaPost(int idOdgP, Rakcija rakcija, String timestamp, int idKorP, int idPost) {
        this.idOdgP = idOdgP;
        this.rakcija = rakcija;
        this.timestamp = timestamp;
        this.idKorP = idKorP;
        this.idPost = idPost;
    }

    public OdgNaPost(){

    }

    public int getIdOdgP() {
        return idOdgP;
    }

    public void setIdOdgP(int idOdgP) {
        this.idOdgP = idOdgP;
    }

    public Rakcija getRakcija() { return rakcija;
    }

    public void setRakcija(Rakcija rakcija) {
        this.rakcija = rakcija;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getIdKorP() {
        return idKorP;
    }

    public void setIdKorP(int idKorP) {
        this.idKorP = idKorP;
    }

    public int getIdPost() {
        return idPost;
    }

    public void setIdPost(int idPost) {
        this.idPost = idPost;
    }
}
