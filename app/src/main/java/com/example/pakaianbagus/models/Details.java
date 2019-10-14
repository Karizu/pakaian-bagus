package com.example.pakaianbagus.models;

public class Details {
    String qty;
    String price;
    String stock_id;

    public Details(String qty, String price, String stock_id) {
        this.qty = qty;
        this.price = price;
        this.stock_id = stock_id;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }
}
