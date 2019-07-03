package com.example.pakaianbagus.models;

public class Role {

    private String id;
    private String name;
    private String permissions;
    private String created_at;
    private String updated_at;
    private String is_gudang;
    private String is_store;
    private String is_leader;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getIs_gudang() {
        return is_gudang;
    }

    public void setIs_gudang(String is_gudang) {
        this.is_gudang = is_gudang;
    }

    public String getIs_store() {
        return is_store;
    }

    public void setIs_store(String is_store) {
        this.is_store = is_store;
    }

    public String getIs_leader() {
        return is_leader;
    }

    public void setIs_leader(String is_leader) {
        this.is_leader = is_leader;
    }
}
