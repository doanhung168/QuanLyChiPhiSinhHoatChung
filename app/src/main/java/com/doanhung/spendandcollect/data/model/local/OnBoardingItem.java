package com.doanhung.spendandcollect.data.model.local;

import android.os.Parcel;

import java.io.Serializable;

public class OnBoardingItem implements Serializable {
    private String title, description;
    private int image;


    public OnBoardingItem(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    protected OnBoardingItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        image = in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
