package com.example.pakaianbagus.models;

import com.example.pakaianbagus.models.user.Group;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Profile extends RealmObject {
    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("treatment_id")
    @Expose
    private String treatmentId;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    @SerializedName("identity_card")
    @Expose
    private String identityCard;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("birth_place")
    @Expose
    private String birthPlace;

    @SerializedName("birth_date")
    @Expose
    private String birthDate;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("picture")
    @Expose
    private String picture;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
