package com.example.pakaianbagus.models;

public class KunjunganKoordinatorModel {

    private String id;
    private String role_id;
    private String first_name;
    private String email;

    public KunjunganKoordinatorModel(String id, String role_id, String first_name, String email) {
        this.id = id;
        this.role_id = role_id;
        this.first_name = first_name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
