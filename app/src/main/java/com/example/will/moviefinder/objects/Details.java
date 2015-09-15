package com.example.will.moviefinder.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by will on 9/13/2015.
 */
public class Details implements Parcelable {
    private String origTitle;
    private String desc;
    private String releaseDate;
    private String runTime;
    private String posterUrl;
    private String rating;

    public Details(String origTitle, String desc, String releaseDate, String runTime,
                   String posterUrl, String rating){
        this.origTitle = origTitle;
        this.desc = desc;
        this.releaseDate = releaseDate;
        this.runTime = runTime;
        this.posterUrl = posterUrl;
        this.rating = rating;
    }

    public Details(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.origTitle = data[0];
        this.desc= data[1];
        this.releaseDate= data[2];
        this.runTime= data[3];
        this.posterUrl= data[4];
        this.rating= data[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.origTitle,
                this.desc,
                this.releaseDate,
                this.runTime,
                this.posterUrl,
                this.rating});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getOrigTitle() {
        return origTitle;
    }

    public String getDesc() {
        return desc;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRunTime() {
        return runTime;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setOrigTitle(String origTitle) {
        this.origTitle = origTitle;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
