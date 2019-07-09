package com.example.pakaianbagus.models;

import java.util.List;

public class PenerimaanBarangResponse {
    private String id_penerimaan;
    private String no_bukti_penerimaan;
    private String tanggal;
    private String created_on;
    private String updated_on;
    private String total_barang;
    private String total_harga;
    private String status;
    private String id_pengiriman;
    private String total_barang_diterima;
    private List<StokPenerimaan> stok_artikel;

    public String getId_penerimaan() {
        return id_penerimaan;
    }

    public void setId_penerimaan(String id_penerimaan) {
        this.id_penerimaan = id_penerimaan;
    }

    public String getNo_bukti_penerimaan() {
        return no_bukti_penerimaan;
    }

    public void setNo_bukti_penerimaan(String no_bukti_penerimaan) {
        this.no_bukti_penerimaan = no_bukti_penerimaan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getTotal_barang() {
        return total_barang;
    }

    public void setTotal_barang(String total_barang) {
        this.total_barang = total_barang;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_pengiriman() {
        return id_pengiriman;
    }

    public void setId_pengiriman(String id_pengiriman) {
        this.id_pengiriman = id_pengiriman;
    }

    public String getTotal_barang_diterima() {
        return total_barang_diterima;
    }

    public void setTotal_barang_diterima(String total_barang_diterima) {
        this.total_barang_diterima = total_barang_diterima;
    }

    public List<StokPenerimaan> getStok_artikel() {
        return stok_artikel;
    }

    public void setStok_artikel(List<StokPenerimaan> stok_artikel) {
        this.stok_artikel = stok_artikel;
    }
}
