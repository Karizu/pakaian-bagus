package com.example.pakaianbagus.models.mutation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MutationResponse {


    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("m_expedition_id")
    @Expose
    private int mExpeditionId;
    @SerializedName("no")
    @Expose
    private String no;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("total_qty")
    @Expose
    private int totalQty;
    @SerializedName("total_price")
    @Expose
    private int totalPrice;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("receipt_no")
    @Expose
    private String receiptNo;
    @SerializedName("receipt_note")
    @Expose
    private String receiptNote;
    @SerializedName("receipt_proof")
    @Expose
    private String receiptProof;
    @SerializedName("status")
    @Expose
    private int status;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMExpeditionId() {
        return mExpeditionId;
    }

    public void setMExpeditionId(int mExpeditionId) {
        this.mExpeditionId = mExpeditionId;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(int totalQty) {
        this.totalQty = totalQty;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getReceiptNote() {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote) {
        this.receiptNote = receiptNote;
    }

    public String getReceiptProof() {
        return receiptProof;
    }

    public void setReceiptProof(String receiptProof) {
        this.receiptProof = receiptProof;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
