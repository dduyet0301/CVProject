package com.t3h.buoi11.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class News {

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo
    @SerializedName("description")
    private String description;

    @NonNull
    @PrimaryKey
    @SerializedName("url")
    private String url;

    @ColumnInfo
    @SerializedName("urlToImage")
    private String img;

    @ColumnInfo
    @SerializedName("publishedAt")
    private String publish;

    @ColumnInfo
    private boolean isFavor;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public void setFavor(boolean favor) {
        isFavor = favor;
    }
    public boolean isFavor(){
        return isFavor;
    }
}
