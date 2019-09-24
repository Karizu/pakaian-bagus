package com.example.pakaianbagus.models;

import com.example.pakaianbagus.models.api.salesreport.Detail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alfianhpratama on 10/09/2019.
 * Organization: UTeam
 */
public class SalesReport {
    @SerializedName("sales_id")
    @Expose
    private String salesId;
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("total_qty")
    @Expose
    private String totalQty;
    @SerializedName("total_price")
    @Expose
    private String totalPrice;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(String totalQty) {
        this.totalQty = totalQty;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}

