package com.example.pakaianbagus.models;

public class StokPenerimaan {
    private String id_penerimaan_detail;
    private String id_stok_toko;
    private String id_penerimaan;
    private String no_seri;
    private String qty;
    private String nama_barang;

    public String getId_penerimaan_detail() {
        return id_penerimaan_detail;
    }

    public void setId_penerimaan_detail(String id_penerimaan_detail) {
        this.id_penerimaan_detail = id_penerimaan_detail;
    }

    public String getId_stok_toko() {
        return id_stok_toko;
    }

    public void setId_stok_toko(String id_stok_toko) {
        this.id_stok_toko = id_stok_toko;
    }

    public String getId_penerimaan() {
        return id_penerimaan;
    }

    public void setId_penerimaan(String id_penerimaan) {
        this.id_penerimaan = id_penerimaan;
    }

    public String getNo_seri() {
        return no_seri;
    }

    public void setNo_seri(String no_seri) {
        this.no_seri = no_seri;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }
}
