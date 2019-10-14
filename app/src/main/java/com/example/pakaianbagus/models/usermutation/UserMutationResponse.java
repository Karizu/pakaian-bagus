
package com.example.pakaianbagus.models.usermutation;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMutationResponse implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("from_group_id")
    @Expose
    private Integer fromGroupId;
    @SerializedName("to_group_id")
    @Expose
    private Integer toGroupId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("user")
    @Expose
    private com.example.pakaianbagus.models.usermutation.User user;
    @SerializedName("from_group")
    @Expose
    private FromGroup fromGroup;
    @SerializedName("to_group")
    @Expose
    private ToGroup toGroup;
    private final static long serialVersionUID = 5385138096416525661L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFromGroupId() {
        return fromGroupId;
    }

    public void setFromGroupId(Integer fromGroupId) {
        this.fromGroupId = fromGroupId;
    }

    public Integer getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(Integer toGroupId) {
        this.toGroupId = toGroupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FromGroup getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(FromGroup fromGroup) {
        this.fromGroup = fromGroup;
    }

    public ToGroup getToGroup() {
        return toGroup;
    }

    public void setToGroup(ToGroup toGroup) {
        this.toGroup = toGroup;
    }

}
