package com.example.redditcloneandroid.model;

public class Pravilo {

    private int idPra;
    private String oPravilu;
    private int zajednica;

    public Pravilo(int idPra, String oPravilu, int zajednica) {
        this.idPra = idPra;
        this.oPravilu = oPravilu;
        this.zajednica = zajednica;
    }

    public Pravilo(){

    }

    public int getIdPra() {
        return idPra;
    }

    public void setIdPra(int idPra) {
        this.idPra = idPra;
    }

    public String getoPravilu() {
        return oPravilu;
    }

    public void setoPravilu(String oPravilu) {
        this.oPravilu = oPravilu;
    }

    public int getZajednica() {
        return zajednica;
    }

    public void setZajednica(int zajednica) {
        this.zajednica = zajednica;
    }

}
