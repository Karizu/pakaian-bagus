package com.example.pakaianbagus.models;

public class SpgModel {

    private String id;
    private String name;
    private String toko;
    private String fromToko;
    private String toToko;
    private int status;

    public SpgModel(String id, String name, String toko, int status) {
        this.id = id;
        this.name = name;
        this.toko = toko;
        this.status = status;
    }

    public SpgModel(String id, String name, String toko) {
        this.id = id;
        this.name = name;
        this.toko = toko;
    }

    public SpgModel(String id, String name, String fromToko, String toToko, int status) {
        this.id = id;
        this.name = name;
        this.fromToko = fromToko;
        this.toToko = toToko;
        this.status = status;
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

    public String getToko() {
        return toko;
    }

    public void setToko(String toko) {
        this.toko = toko;
    }

    public String getFromToko() {
        return fromToko;
    }

    public void setFromToko(String fromToko) {
        this.fromToko = fromToko;
    }

    public String getToToko() {
        return toToko;
    }

    public void setToToko(String toToko) {
        this.toToko = toToko;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
