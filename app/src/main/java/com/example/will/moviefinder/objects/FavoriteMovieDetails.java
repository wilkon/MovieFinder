package com.example.will.moviefinder.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by will on 1/31/16.
 */
public class FavoriteMovieDetails extends MovieDetails{


    String movieId;
    String poster_img;

    static final int KEY_POSTER_IMG = 6;
    static final int KEY_MOVIE_ID = 8;

    public FavoriteMovieDetails(String origTitle, String desc, String releaseDate, String runTime,
                                String posterUrl, String rating, String poster_img, String movieId){
        super(origTitle, desc, releaseDate, runTime, posterUrl, rating, poster_img, movieId);
        this.poster_img = poster_img;
        this.movieId = movieId;
    }

    public FavoriteMovieDetails(Parcel in){
        super(in);

        String[] data = new String[9];
        in.readStringArray(data);

        this.poster_img = data[KEY_POSTER_IMG];
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

    public String getPoster_img() {
        return poster_img;
    }

    public void setPoster_img(String poster_img) {
        this.poster_img = poster_img;
    }


    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
