package com.example.will.moviefinder.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by will on 9/13/2015.
 */
public class MovieDetails implements Parcelable {

    private String origTitle;
    private String desc;
    private String releaseDate;
    private String runTime;
    private String posterUrl;
    private String rating;
    private String movieId;
    private String poster_img;

    static final int KEY_ORIG_TITLE = 0;
    static final int KEY_DESC = 1;
    static final int KEY_RELEASE_DATE = 2;
    static final int KEY_RUN_TIME = 3;
    static final int KEY_POSTER_URL = 4;
    static final int KEY_RATING = 5;
    static final int KEY_POSTER_IMG = 6;
    static final int KEY_MOVIE_ID = 7;

    public MovieDetails(String origTitle, String desc, String releaseDate, String runTime,
                        String posterUrl, String rating, String poster_img,String movieId) {
        this.origTitle = origTitle;
        this.desc = desc;
        this.releaseDate = releaseDate;
        this.runTime = runTime;
        this.posterUrl = posterUrl;
        this.rating = rating;
        this.poster_img = poster_img;
        this.movieId = movieId;
    }

    public MovieDetails(Parcel in) {
        String[] data = new String[8];

        in.readStringArray(data);
        this.origTitle = data[KEY_ORIG_TITLE];
        this.desc = data[KEY_DESC];
        this.releaseDate = data[KEY_RELEASE_DATE];
        this.runTime = data[KEY_RUN_TIME];
        this.posterUrl = data[KEY_POSTER_URL];
        this.rating = data[KEY_RATING];
        this.poster_img = data[KEY_POSTER_IMG];
        this.movieId = data[KEY_MOVIE_ID];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.origTitle,
                this.desc,
                this.releaseDate,
                this.runTime,
                this.posterUrl,
                this.rating,
                this.poster_img,
                this.movieId});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
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

    public String getPoster_img() {
        return poster_img;
    }

    public String getMovieId() {
        return movieId;
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


    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPoster_img(String poster_img) {
        this.poster_img = poster_img;
    }

}
