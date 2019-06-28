package com.example.pakaianbagus.models;

public class InventarisModel {
    private String id;
    private String name;
    private String qty;

    public InventarisModel(String name, String qty) {
        this.name = name;
        this.qty = qty;
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
}
