package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnnouncementResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("announcement_category_id")
    @Expose
    private String announcement_category_id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnnouncement_category_id() {
        return announcement_category_id;
    }

    public void setAnnouncement_category_id(String announcement_category_id) {
        this.announcement_category_id = announcement_category_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
