package com.example.pakaianbagus.models;

public class BarangMasukModel {
    private String id;
    private String kodeArtikel;
    private String qty;
    private String status;

    public BarangMasukModel(String id, String kodeArtikel, String qty, String status) {
        this.id = id;
        this.kodeArtikel = kodeArtikel;
        this.qty = qty;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKodeArtikel() {
        return kodeArtikel;
    }

    public void setKodeArtikel(String kodeArtikel) {
        this.kodeArtikel = kodeArtikel;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
