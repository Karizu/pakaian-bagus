package com.example.pakaianbagus.models;

import java.util.List;

public class TransactionModel {
    String sales_id;
    String member_id;
    String no;
    String date;
    String from;
    String total_qty;
    String total_price;
    String description;
    String type;
    String payment_method;
    List<Details> details;

    public TransactionModel(String sales_id, String member_id, String no, String date, String from, String total_qty, String total_price, String description, String type, String payment_method, List<Details> details) {
        this.sales_id = sales_id;
        this.member_id = member_id;
        this.no = no;
        this.date = date;
        this.from = from;
        this.total_qty = total_qty;
        this.total_price = total_price;
        this.description = description;
        this.type = type;
        this.payment_method = payment_method;
        this.details = details;
    }

    public String getSales_id() {
        return sales_id;
    }

    public void setSales_id(String sales_id) {
        this.sales_id = sales_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(String total_qty) {
        this.total_qty = total_qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }
}
