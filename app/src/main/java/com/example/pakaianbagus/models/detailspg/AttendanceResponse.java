
package com.example.pakaianbagus.models.detailspg;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceResponse implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("m_group_id")
    @Expose
    private Integer mGroupId;
    @SerializedName("m_schedule_id")
    @Expose
    private Integer mScheduleId;
    @SerializedName("check_in")
    @Expose
    private String checkIn;
    @SerializedName("check_out")
    @Expose
    private String checkOut;
    @SerializedName("photo_check_in")
    @Expose
    private String photoCheckIn;
    @SerializedName("photo_check_out")
    @Expose
    private String photoCheckOut;
    @SerializedName("long_check_in")
    @Expose
    private String longCheckIn;
    @SerializedName("long_check_out")
    @Expose
    private String longCheckOut;
    @SerializedName("lat_check_in")
    @Expose
    private String latCheckIn;
    @SerializedName("lat_check_out")
    @Expose
    private String latCheckOut;
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
    private User user;
    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    private final static long serialVersionUID = 6205930048957802108L;

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

    public Integer getMGroupId() {
        return mGroupId;
    }

    public void setMGroupId(Integer mGroupId) {
        this.mGroupId = mGroupId;
    }

    public Integer getMScheduleId() {
        return mScheduleId;
    }

    public void setMScheduleId(Integer mScheduleId) {
        this.mScheduleId = mScheduleId;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getPhotoCheckIn() {
        return photoCheckIn;
    }

    public void setPhotoCheckIn(String photoCheckIn) {
        this.photoCheckIn = photoCheckIn;
    }

    public String getPhotoCheckOut() {
        return photoCheckOut;
    }

    public void setPhotoCheckOut(String photoCheckOut) {
        this.photoCheckOut = photoCheckOut;
    }

    public String getLongCheckIn() {
        return longCheckIn;
    }

    public void setLongCheckIn(String longCheckIn) {
        this.longCheckIn = longCheckIn;
    }

    public String getLongCheckOut() {
        return longCheckOut;
    }

    public void setLongCheckOut(String longCheckOut) {
        this.longCheckOut = longCheckOut;
    }

    public String getLatCheckIn() {
        return latCheckIn;
    }

    public void setLatCheckIn(String latCheckIn) {
        this.latCheckIn = latCheckIn;
    }

    public String getLatCheckOut() {
        return latCheckOut;
    }

    public void setLatCheckOut(String latCheckOut) {
        this.latCheckOut = latCheckOut;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

}
