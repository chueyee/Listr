package com.example.listr;

import java.util.Date;
import java.util.UUID;

public class Item {
    private UUID mId;
    private String mTitle, mDetail;
    private Date mDate;
    private boolean mHave;

    public Item() {
        this(UUID.randomUUID());
    }

    public Item(UUID id) {
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDetail() {
        return mDetail;
    }

    public void setDetail(String detail) {
        mDetail = detail;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setHave(boolean have) {
        mHave = have;
    }

    public boolean isHave() {
        return mHave;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}
