package com.dc.myapplication.model;

import java.util.Date;
import java.util.UUID;

public class Life {

    private UUID Id;
    private String Title;
    private Date Date;
    private String Description;
    private boolean Star;

    public Life() {
        this(UUID.randomUUID());
    }

    public Life(UUID id) {
        Id = id;
        Date = new Date();
    }

    public UUID getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isStar() {
        return Star;
    }

    public void setStar(boolean star) {
        Star = star;
    }

    String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
