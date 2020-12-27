package com.ubb.tpjad.photoalbum.response;

import java.sql.Date;

public class PhotoResponse {
    private int id;
    private String name;
    private Date date;
    private byte[] content;

    public PhotoResponse(int id, String name, Date date, byte[] content) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
