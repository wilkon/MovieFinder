package com.example.will.moviefinder.objects;

/**
 * Created by will on 1/31/16.
 */
public class FavoriteMovieDetails extends MovieDetails{


    String movieId;
    byte[] poster_img;
    byte[] poster_img_mini;

    public FavoriteMovieDetails(String origTitle, String desc, String releaseDate, String runTime,
                                String posterUrl, String rating, byte[] poster_img, byte[] poster_img_mini, String movieId){
        super(origTitle, desc, releaseDate, runTime, posterUrl, rating);
        this.poster_img = poster_img;
        this.poster_img_mini = poster_img_mini;
        this.movieId = movieId;
    }

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
