
package com.example.pakaianbagus.models.api.stockopname;

import com.example.pakaianbagus.models.Brand;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("m_specification_id")
    @Expose
    private int mSpecificationId;
    @SerializedName("m_brand_id")
    @Expose
    private int mBrandId;
    @SerializedName("m_pattern_id")
    @Expose
    private int mPatternId;
    @SerializedName("m_category_id")
    @Expose
    private int mCategoryId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("spec")
    @Expose
    private Spec spec;
    @SerializedName("brand")
    @Expose
    private com.example.pakaianbagus.models.Brand brand;
    @SerializedName("pattern")
    @Expose
    private Pattern pattern;
    @SerializedName("category")
    @Expose
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMSpecificationId() {
        return mSpecificationId;
    }

    public void setMSpecificationId(int mSpecificationId) {
        this.mSpecificationId = mSpecificationId;
    }

    public int getMBrandId() {
        return mBrandId;
    }

    public void setMBrandId(int mBrandId) {
        this.mBrandId = mBrandId;
    }

    public int getMPatternId() {
        return mPatternId;
    }

    public void setMPatternId(int mPatternId) {
        this.mPatternId = mPatternId;
    }

    public int getMCategoryId() {
        return mCategoryId;
    }

    public void setMCategoryId(int mCategoryId) {
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

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    public com.example.pakaianbagus.models.Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}