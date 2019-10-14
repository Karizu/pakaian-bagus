package com.example.pakaianbagus.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CounterResponse implements Serializable
{

    @SerializedName("m_place_id")
    @Expose
    private String mPlaceId;
    @SerializedName("m_brand_id")
    @Expose
    private String mBrandId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("counter_id")
    @Expose
    private String counterId;
    @SerializedName("img_front_view")
    @Expose
    private String imgFrontView;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    private final static long serialVersionUID = 4229344926237778705L;

    public String getMPlaceId() {
        return mPlaceId;
    }

    public void setMPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public String getMBrandId() {
        return mBrandId;
    }

    public void setMBrandId(String mBrandId) {
        this.mBrandId = mBrandId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCounterId() {
        return counterId;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    public String getImgFrontView() {
        return imgFrontView;
    }

    public void setImgFrontView(String imgFrontView) {
        this.imgFrontView = imgFrontView;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}