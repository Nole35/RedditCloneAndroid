package com.example.redditcloneandroid.model;

import java.time.LocalDate;

public class Ban {

    private int idBan;
    private int zajednica;
    private String razlog;
    private String timestamp;
    private int banKor;

    public Ban(int idBan, String timestamp, int banKor, int zajednica, String razlog) {
        this.idBan = idBan;
        this.zajednica = zajednica;
        this.razlog = razlog;
        this.timestamp = timestamp;
        this.banKor = banKor;

    }

    public Ban(){

    }

    public int getIdBan() {
        return idBan;
    }

    public void setIdBan(int idBan) {
        this.idBan = idBan;
    }

    public int getZajednica() {
        return zajednica;
    }

    public void setZajednica(int zajednica) {
        this.zajednica = zajednica;
    }

    public String getRazlog() {
        return razlog;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getBanKor() {
        return banKor;
    }

    public void setBanKor(int banKor) {
        this.banKor = banKor;
    }
}
