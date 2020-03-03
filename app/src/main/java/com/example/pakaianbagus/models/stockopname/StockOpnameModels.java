package com.example.pakaianbagus.models.stockopname;

import java.util.List;

public class StockOpnameModels {

    private String m_place_id;
    private String m_brand_id;
    private String place_type;
    private String date;
    private List<Details> details;

    public String getM_place_id() {
        return m_place_id;
    }

    public void setM_place_id(String m_place_id) {
        this.m_place_id = m_place_id;
    }

    public String getM_brand_id() {
        return m_brand_id;
    }

    public void setM_brand_id(String m_brand_id) {
        this.m_brand_id = m_brand_id;
    }

    public String getPlace_type() {
        return place_type;
    }

    public void setPlace_type(String place_type) {
        this.place_type = place_type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }
}
