package com.example.pakaianbagus.models;

public class DetailInvoiceModel {
    int id;
    int qty;
    int price;
    String name;
    String image;
    int total;

    public DetailInvoiceModel(int id, int qty, int price, String name, String image, int total) {
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.name = name;
        this.image = image;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
