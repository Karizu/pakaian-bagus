
package com.example.pakaianbagus.models.api.stockopname;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockCategoryResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("m_place_id")
    @Expose
    private int mPlaceId;
    @SerializedName("type")
    @Expose
    private int type;
    @SerializedName("m_item_id")
    @Expose
    private int mItemId;
    @SerializedName("m_category_id")
    @Expose
    private int mCategoryId;
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
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("size")
    @Expose
    private Size size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMPlaceId() {
        return mPlaceId;
    }

    public void setMPlaceId(int mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
