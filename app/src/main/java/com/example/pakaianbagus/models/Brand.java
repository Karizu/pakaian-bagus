package com.example.pakaianbagus.models;

public class Brand {

    private String id;
    private String nama_brand;
    private String code;
    private String deskripsi;
    private String gambar;
    private String jenis_brand;

    public Brand(String id_brand, String nama_brand, String code) {
        this.id = id_brand;
        this.nama_brand = nama_brand;
        this.code = code;
    }

    public Brand(String id_brand, String nama_brand, String deskripsi, String gambar, String jenis_brand) {
        this.id = id_brand;
        this.nama_brand = nama_brand;
        this.deskripsi = deskripsi;
        this.gambar = gambar;
        this.jenis_brand = jenis_brand;
    }

    public String getId_brand() {
        return id;
    }

    public void setId_brand(String id_brand) {
        this.id = id_brand;
    }

    public String getNama_brand() {
        return nama_brand;
    }

    public void setNama_brand(String nama_brand) {
        this.nama_brand = nama_brand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJenis_brand() {
        return jenis_brand;
    }

    public void setJenis_brand(String jenis_brand) {
        this.jenis_brand = jenis_brand;
    }
}
