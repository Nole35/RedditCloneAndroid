package com.example.redditcloneandroid.model;

import com.example.redditcloneandroid.model.enums.Rakcija;

public class OdgNaKom {

    private int idOdg;
    private Rakcija rakcija;
    private String timestamp;
    private int idKor;
    private int idKom;

    public OdgNaKom(int idOdg, Rakcija rakcija, String timestamp, int idKor, int idKom) {
        this.idOdg = idOdg;
        this.rakcija = rakcija;
        this.timestamp = timestamp;
        this.idKor = idKor;
        this.idKom = idKom;
    }

    public OdgNaKom(){

    }

    public int getIdOdg() {
        return idOdg;
    }

    public void setIdOdg(int idOdg) {
        this.idOdg = idOdg;
    }

    public Rakcija getRakcija() {
        return rakcija;
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

    public int getIdKor() {
        return idKor;
    }

    public void setIdKor(int idKor) {
        this.idKor = idKor;
    }

    public int getIdKom() {
        return idKom;
    }

    public void setIdKom(int idKom) {
        this.idKom = idKom;
    }
}
