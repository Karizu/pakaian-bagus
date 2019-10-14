package com.example.pakaianbagus.models;

import java.util.List;

import okhttp3.RequestBody;

public class StandHanger {
    private String counter_id;
    private List<RequestBody> attachment;

    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    public List<RequestBody> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<RequestBody> attachment) {
        this.attachment = attachment;
    }
}
