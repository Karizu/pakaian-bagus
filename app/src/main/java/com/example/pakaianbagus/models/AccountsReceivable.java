
package com.example.pakaianbagus.models;

import java.io.Serializable;
import java.lang.reflect.Member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountsReceivable implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("m_member_id")
    @Expose
    private String mMemberId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("transaction")
    @Expose
    private Transaction transaction;
    @SerializedName("member")
    @Expose
    private Member member;
    private final static long serialVersionUID = -1975940693412124417L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMMemberId() {
        return mMemberId;
    }

    public void setMMemberId(String mMemberId) {
        this.mMemberId = mMemberId;
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

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
