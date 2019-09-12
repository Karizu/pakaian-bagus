package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockOpnameModel {
    @SerializedName("m_place_id")
    @Expose
    private String mPlaceId;
    @SerializedName("m_item_id")
    @Expose
    private String mItemId;
    @SerializedName("place_type")
    @Expose
    private String placeType;
    @SerializedName("series_number")
    @Expose
    private String seriesNumber;
    @SerializedName("size_code")
    @Expose
    private String sizeCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("price")
    @Expose
    private int price;

    public String getMPlaceId() {
        return mPlaceId;
    }

    public void setMPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public String getMItemId() {
        return mItemId;
    }

    public void setMItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
