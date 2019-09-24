package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockOpnameModel {
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("m_place_id")
    @Expose
    private String mPlaceId;
    @SerializedName("m_item_id")
    @Expose
    private String mItemId;
    @SerializedName("m_category_id")
    @Expose
    private String mCategoryId;
    @SerializedName("m_brand_id")
    @Expose
    private String mBrandId;
    @SerializedName("place_type")
    @Expose
    private String placeType;
    @SerializedName("article_code")
    @Expose
    private String articleCode;
    @SerializedName("size_code")
    @Expose
    private String sizeCode;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("price")
    @Expose
    private int price;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getmPlaceId() {
        return mPlaceId;
    }

    public void setmPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public String getmItemId() {
        return mItemId;
    }

    public void setmItemId(String mItemId) {
        this.mItemId = mItemId;
    }

    public String getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(String mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getmBrandId() {
        return mBrandId;
    }

    public void setmBrandId(String mBrandId) {
        this.mBrandId = mBrandId;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getArticleCode() {
        return articleCode;
    }

    public void setArticleCode(String articleCode) {
        this.articleCode = articleCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
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
