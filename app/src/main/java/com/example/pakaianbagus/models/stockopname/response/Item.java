
package com.example.pakaianbagus.models.stockopname.response;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("m_specification_id")
    @Expose
    private Integer mSpecificationId;
    @SerializedName("m_brand_id")
    @Expose
    private Integer mBrandId;
    @SerializedName("m_pattern_id")
    @Expose
    private Integer mPatternId;
    @SerializedName("m_category_id")
    @Expose
    private Integer mCategoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
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
    private final static long serialVersionUID = 3646495368440745605L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMSpecificationId() {
        return mSpecificationId;
    }

    public void setMSpecificationId(Integer mSpecificationId) {
        this.mSpecificationId = mSpecificationId;
    }

    public Integer getMBrandId() {
        return mBrandId;
    }

    public void setMBrandId(Integer mBrandId) {
        this.mBrandId = mBrandId;
    }

    public Integer getMPatternId() {
        return mPatternId;
    }

    public void setMPatternId(Integer mPatternId) {
        this.mPatternId = mPatternId;
    }

    public Integer getMCategoryId() {
        return mCategoryId;
    }

    public void setMCategoryId(Integer mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

}
