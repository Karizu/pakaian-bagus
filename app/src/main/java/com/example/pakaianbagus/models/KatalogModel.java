package com.example.pakaianbagus.models;

public class KatalogModel {

    private String id;
    private String name;
    private String image;
    private String qty;
    private String kode;

    public KatalogModel(String id, String name, String image, String qty, String kode) {
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }
}
