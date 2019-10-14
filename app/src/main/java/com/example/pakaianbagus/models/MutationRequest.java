package com.example.pakaianbagus.models;

import com.example.pakaianbagus.models.api.mutation.detail.Detail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alfianhpratama on 14/09/2019.
 * Organization: UTeam
 */
public class MutationRequest {
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
    private int from;
    @SerializedName("to")
    @Expose
    private int to;
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
    @SerializedName("m_brand_id")
    @Expose
    private int m_brand_id;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public int getmExpeditionId() {
        return mExpeditionId;
    }

    public void setmExpeditionId(int mExpeditionId) {
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

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
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


    public int getM_brand_id() {
        return m_brand_id;
    }

    public void setM_brand_id(int m_brand_id) {
        this.m_brand_id = m_brand_id;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
