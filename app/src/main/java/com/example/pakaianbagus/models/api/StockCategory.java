package com.example.pakaianbagus.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alfianhpratama on 24/09/2019.
 * Organization: UTeam
 */
public class StockCategory {

    @SerializedName("m_place_id")
    @Expose
    private int mPlaceId;
    @SerializedName("m_item_id")
    @Expose
    private int mItemId;
    @SerializedName("m_category_id")
    @Expose
    private int mCategoryId;
    @SerializedName("m_brand_id")
    @Expose
    private int mBrandId;
    @SerializedName("name")
    @Expose
    private String name;

    public int getMPlaceId() {
        return mPlaceId;
    }

    public void setMPlaceId(int mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public int getMItemId() {
        return mItemId;
    }

    public void setMItemId(int mItemId) {
        this.mItemId = mItemId;
    }

    public int getMCategoryId() {
        return mCategoryId;
    }

    public void setMCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public int getMBrandId() {
        return mBrandId;
    }

    public void setMBrandId(int mBrandId) {
        this.mBrandId = mBrandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
