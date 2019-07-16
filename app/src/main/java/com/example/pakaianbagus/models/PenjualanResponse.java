package com.example.pakaianbagus.models;

import java.util.List;

public class PenjualanResponse {

    private String id_transaksi;
    private String tanggal ;
    private String total_harga;
    private String no_faktur_penjualan;
    private List<TransaksiDetail> transaksi_detail;

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getNo_faktur_penjualan() {
        return no_faktur_penjualan;
    }

    public void setNo_faktur_penjualan(String no_faktur_penjualan) {
        this.no_faktur_penjualan = no_faktur_penjualan;
    }

    public List<TransaksiDetail> getTransaksi_detail() {
        return transaksi_detail;
    }

    public void setTransaksi_detail(List<TransaksiDetail> transaksi_detail) {
        this.transaksi_detail = transaksi_detail;
    }

    public class TransaksiDetail {
        private String id_transaksi_detail;
        private String id_stok_toko;
        private String id_transaksi;
        private String qty;
        private String harga;
        private String discount;
        private String ukuran;

        public String getId_transaksi_detail() {
            return id_transaksi_detail;
        }

        public void setId_transaksi_detail(String id_transaksi_detail) {
            this.id_transaksi_detail = id_transaksi_detail;
        }

        public String getId_stok_toko() {
            return id_stok_toko;
        }

        public void setId_stok_toko(String id_stok_toko) {
            this.id_stok_toko = id_stok_toko;
        }

        public String getId_transaksi() {
            return id_transaksi;
        }

        public void setId_transaksi(String id_transaksi) {
            this.id_transaksi = id_transaksi;
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

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getUkuran() {
            return ukuran;
        }

        public void setUkuran(String ukuran) {
            this.ukuran = ukuran;
        }
    }
}
