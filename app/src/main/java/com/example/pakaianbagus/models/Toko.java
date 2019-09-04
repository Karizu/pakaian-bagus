package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Toko {

    @SerializedName("id_store")
    @Expose
    private String id;
    @SerializedName("nama_store")
    @Expose
    private String name;
    @SerializedName("alamat")
    @Expose
    private String datetime;

    public Toko(String name, String datetime) {
        this.name = name;
        this.datetime = datetime;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}