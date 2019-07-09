package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokoResponse {

    @SerializedName("id_store")
    @Expose
    private String id;
    @SerializedName("nama_store")
    @Expose
    private String name;
    @SerializedName("alamat")
    @Expose
    private String alamat;

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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
