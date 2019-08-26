package com.example.pakaianbagus.models;

import android.graphics.Bitmap;

public class PlaceWorkModel {

    private String photo;
    private Bitmap img;

    public PlaceWorkModel(String photo) {
        this.photo = photo;
    }

    public PlaceWorkModel() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
