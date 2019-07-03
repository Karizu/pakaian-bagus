package com.example.pakaianbagus.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private String id;

    @SerializedName("role_id")
    @Expose
    private String roleId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("username")
    @Expose
    private Integer username;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUsername() {
        return username;
    }

    public void setUsername(Integer status) {
        this.username = status;
    }
}
