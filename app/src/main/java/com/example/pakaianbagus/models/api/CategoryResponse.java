package com.example.pakaianbagus.models.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alfianhpratama on 21/09/2019.
 * Organization: UTeam
 */
public class CategoryResponse {
    @SerializedName("m_specification_id")
    @Expose
    private int mSpecificationId;
    @SerializedName("m_category_id")
    @Expose
    private int mCategoryId;
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
    @SerializedName("spec")
    @Expose
    private Spec spec;
    @SerializedName("Category")
    @Expose
    private Category category;

    public int getMSpecificationId() {
        return mSpecificationId;
    }

    public void setMSpecificationId(int mSpecificationId) {
        this.mSpecificationId = mSpecificationId;
    }

    public int getMCategoryId() {
        return mCategoryId;
    }

    public void setMCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
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

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}