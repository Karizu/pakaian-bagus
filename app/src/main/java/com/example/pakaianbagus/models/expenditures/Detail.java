package com.example.pakaianbagus.models.expenditures;

public class Detail {
    private String m_expend_id;
    private String description;
    private String amount;
    private String image;

    public String getM_expend_id() {
        return m_expend_id;
    }

    public void setM_expend_id(String m_expend_id) {
        this.m_expend_id = m_expend_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
