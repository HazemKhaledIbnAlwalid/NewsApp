package com.example.android.newsapp;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by Hazem_Khaled on 2017-10-24.
 */

public class News implements Parcelable{
    private String newsTitle;
    private String newsSection;
    private String newsDate;
    private String newsUrl;
    private String newsAuthor;
    public static ArrayList<String> urlList=new ArrayList<>();

    public News(String newsTitle, String newsSection, String newsUrl, String newsDate, String newsAuthor) {
        this.newsTitle = newsTitle;
        this.newsSection = newsSection;
        this.newsUrl = newsUrl;
        this.newsDate=newsDate;
        this.newsAuthor=newsAuthor;
        urlList.add(newsUrl);
    }

    public News(Parcel in) {
        newsTitle=in.readString();
        newsSection=in.readString();
        newsUrl=in.readString();
        newsDate=in.readString();
        newsAuthor=in.readString();
    }

    public String getNewsDate() {
        return newsDate;
    }

    public String getNewsSection() {

        return newsSection;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public String getNewsTitle() {

        return newsTitle;
    }

    public String getNewsAuthor() {
        return newsAuthor;
    }

    @Override
    public String toString() {
        return  newsSection + ' ' + newsTitle  + ' ' +newsUrl+' '+newsDate + ' '+newsAuthor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsTitle);
        dest.writeString(newsSection);
        dest.writeString(newsUrl);
        dest.writeString(newsDate);
        dest.writeString(newsAuthor);
    }
    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
