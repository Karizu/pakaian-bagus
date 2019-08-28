package com.example.pakaianbagus.models;

public class KatalogModel {

    private String id;
    private String name;
    private String image;
    private int qty;
    private int kode;

    public KatalogModel(String id, String name, String image, int qty, int kode) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.qty = qty;
        this.kode = kode;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }
}
