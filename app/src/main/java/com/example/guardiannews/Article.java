package com.example.guardiannews;

//an {@link Article} object contains information related to one article
public class Article {

    //news title
    private String mType;

    //news headline
    private String mTitle;

    //news date
    private String mDate;

    //news web link
    private String mUrl;

    //constructs a new {@link Article} object. @param title of article
    //@param title of article, @param date date of publication @param web link of article
    public Article(String type, String title, String date, String url) {
        mType = type;
        mTitle = title;
        mDate = date;
        mUrl = url;
    }

    //returns type of article
    public String getType() {
        return mType;
    }

    //returns title of article
    public String getTitle() {
        return mTitle;
    }

    //returns date of article
    public String getDate() {
        return mDate;
    }

    //returns web link for article
    public String getUrl() {
        return mUrl;
    }
}
