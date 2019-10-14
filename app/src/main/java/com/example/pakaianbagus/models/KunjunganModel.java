package com.example.pakaianbagus.models;

public class KunjunganModel {
    private String id;
    private String name;
    private String datetime;
    private String image;
    private String nominal;

    public KunjunganModel(String name, String datetime) {
        this.name = name;
        this.datetime = datetime;
    }

    public KunjunganModel(String id, String name, String datetime) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
