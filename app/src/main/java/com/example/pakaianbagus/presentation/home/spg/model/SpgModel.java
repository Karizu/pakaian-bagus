package com.example.pakaianbagus.presentation.home.spg.model;

public class SpgModel {

    private String id;
    private String name;
    private String toko;

    public SpgModel(String name, String toko) {
        this.name = name;
        this.toko = toko;
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

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }
}
