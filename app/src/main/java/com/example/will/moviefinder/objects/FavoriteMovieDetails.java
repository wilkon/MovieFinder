package com.example.will.moviefinder.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by will on 1/31/16.
 */
public class FavoriteMovieDetails extends MovieDetails{


    String movieId;
    byte[] poster_img;
    byte[] poster_img_mini;

    static final int KEY_POSTER_IMG = 6;
    static final int KEY_POSTER_IMG_MINI = 7;
    static final int KEY_MOVIE_ID = 8;

    public FavoriteMovieDetails(String origTitle, String desc, String releaseDate, String runTime,
                                String posterUrl, String rating, byte[] poster_img, byte[] poster_img_mini, String movieId){
        super(origTitle, desc, releaseDate, runTime, posterUrl, rating);
        this.poster_img = poster_img;
        this.poster_img_mini = poster_img_mini;
        this.movieId = movieId;
    }

    public FavoriteMovieDetails(Parcel in){
        super(in);

        String[] data = new String[9];
        in.readStringArray(data);

        this.poster_img = data[KEY_POSTER_IMG].getBytes();
        this.poster_img_mini = data[KEY_POSTER_IMG_MINI].getBytes();
        this.movieId = data[KEY_MOVIE_ID];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FavoriteMovieDetails createFromParcel(Parcel in) {
            return new FavoriteMovieDetails(in);
        }

        public FavoriteMovieDetails[] newArray(int size) {
            return new FavoriteMovieDetails[size];
        }
    };

    public byte[] getPoster_img() {
        return poster_img;
    }

    public void setPoster_img(byte[] poster_img) {
        this.poster_img = poster_img;
    }

    public byte[] getPoster_img_mini() {
        return poster_img_mini;
    }

    public void setPoster_img_mini(byte[] poster_img_mini) {
        this.poster_img_mini = poster_img_mini;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
