package com.example.pakaianbagus.models;

public class BrandResponse {

    private String id;
    private String name;
    private String code;

    public BrandResponse(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /*private String id_brand;
    private String nama_brand;
    private String deskripsi;
    private String gambar;
    private String jenis_brand;

    public String getId_brand() {
        return id_brand;
    }

    public void setId_brand(String id_brand) {
        this.id_brand = id_brand;
    }

    public String getNama_brand() {
        return nama_brand;
    }

    public void setNama_brand(String nama_brand) {
        this.nama_brand = nama_brand;
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
    }*/
}
