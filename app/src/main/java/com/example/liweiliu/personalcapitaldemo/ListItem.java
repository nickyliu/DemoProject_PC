package com.example.liweiliu.personalcapitaldemo;

public class ListItem {
    private String mTitle;
    private String mImage;
    private String mDescription;
    private String mDate;
    private String mLink;

    ListItem() {

    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDate() {
        return mDate;
    }

    public String getLink() {
        return mLink;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setLink(String link) {
        this.mLink = link;
    }

}
