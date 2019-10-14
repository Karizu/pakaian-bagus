package com.example.pakaianbagus.models;

public class KatalogTokoModel {

    private String id;
    private String name;
    private String type;
    private String address;
    private String nominal;
    private String limit;
    private String accounts_receivable;

    public KatalogTokoModel(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public KatalogTokoModel(String nominal, String type) {
        this.nominal = nominal;
        this.type = type;
    }

    public KatalogTokoModel(String id, String name, String address, String limit, String accounts_receivable) {
        this.id = id;
        this.name = name;
        this.address =address;
        this.limit = limit;
        this.accounts_receivable = accounts_receivable;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getAccounts_receivable() {
        return accounts_receivable;
    }

    public void setAccounts_receivable(String accounts_receivable) {
        this.accounts_receivable = accounts_receivable;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
}
