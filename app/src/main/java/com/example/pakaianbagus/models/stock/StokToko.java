package com.example.pakaianbagus.models.stock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StokToko {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("m_place_id")
    @Expose
    private Integer mPlaceId;
    @SerializedName("m_item_id")
    @Expose
    private Integer mItemId;
    @SerializedName("place_type")
    @Expose
    private String placeType;
    @SerializedName("series_number")
    @Expose
    private String seriesNumber;
    @SerializedName("article_code")
    @Expose
    private String articleCode;
    @SerializedName("size_code")
    @Expose
    private String sizeCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("updated_by")
    @Expose
    private Object updatedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("size")
    @Expose
    private Size size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMPlaceId() {
        return mPlaceId;
    }

    public void setMPlaceId(Integer mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public Integer getMItemId() {
        return mItemId;
    }

    public void setMItemId(Integer mItemId) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

}
