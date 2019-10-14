package com.example.pakaianbagus.models.expenditures;

import java.util.List;

import okhttp3.MultipartBody;

public class RequestExpenditures {
    private String user_id;
    private String m_group_id;
    private String date;
    private String m_brand_id;
    private String description;
    private MultipartBody.Part image;
    private List<Detail> details;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getM_group_id() {
        return m_group_id;
    }

    public void setM_group_id(String m_group_id) {
        this.m_group_id = m_group_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getM_brand_id() {
        return m_brand_id;
    }

    public void setM_brand_id(String m_brand_id) {
        this.m_brand_id = m_brand_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
