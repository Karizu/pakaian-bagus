
package com.example.pakaianbagus.models.stockopname.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stock_opname_id")
    @Expose
    private Integer stockOpnameId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("m_item_id")
    @Expose
    private Integer mItemId;
    @SerializedName("m_category_id")
    @Expose
    private Integer mCategoryId;
    @SerializedName("article_code")
    @Expose
    private String articleCode;
    @SerializedName("size_code")
    @Expose
    private String sizeCode;
    @SerializedName("qty")
    @Expose
    private Integer qty;
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
    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("size")
    @Expose
    private Size size;
    private final static long serialVersionUID = 4561682536061752091L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStockOpnameId() {
        return stockOpnameId;
    }

    public void setStockOpnameId(Integer stockOpnameId) {
        this.stockOpnameId = stockOpnameId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMItemId() {
        return mItemId;
    }

    public void setMItemId(Integer mItemId) {
        this.mItemId = mItemId;
    }

    public Integer getMCategoryId() {
        return mCategoryId;
    }

    public void setMCategoryId(Integer mCategoryId) {
        this.mCategoryId = mCategoryId;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

}
