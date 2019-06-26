package com.example.pakaianbagus.presentation.penjualan.model;

public class PenjualanModel {

    private String id;
    private String name;
    private String qty;
    private String harga;
    private String date;

    public PenjualanModel(String name, String qty, String harga, String date) {
        this.name = name;
        this.qty = qty;
        this.harga = harga;
        this.date = date;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
