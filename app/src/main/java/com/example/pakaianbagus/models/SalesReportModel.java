package com.example.pakaianbagus.models;

public class SalesReportModel {
    private String id;
    private String name;
    private String image;
    private String qty;
    private String diskon;
    private String harga;
    private String total;

    public SalesReportModel(String name, String image, String qty, String diskon, String harga, String total) {
        this.name = name;
        this.image = image;
        this.qty = qty;
        this.diskon = diskon;
        this.harga = harga;
        this.total = total;
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

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
