package com.example.pakaianbagus.models;

public class InvoiceModels {
    String id;
    String transaction_id;
    String date;
    String amount;
    String no;

    public InvoiceModels(String id, String transaction_id, String date, String amount, String no) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.date = date;
        this.amount = amount;
        this.no = no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
