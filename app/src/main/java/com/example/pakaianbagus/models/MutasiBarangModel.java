package com.example.pakaianbagus.models;

public class MutasiBarangModel {
    private String id;
    private String name;
    private String datetime;

    public MutasiBarangModel(String name, String datetime) {
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
}
