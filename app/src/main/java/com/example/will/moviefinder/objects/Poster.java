package com.example.will.moviefinder.objects;

/**
 * Created by will on 9/13/2015.
 */
public class Poster {
    private String id;
    private String posterUrl;
    public Poster(String id, String posterUrl){
        this.id=id;
        this.posterUrl = posterUrl;
    }
    public String getId() {
        return id;
    }
    public String getPosterUrl() {
        return posterUrl;
    }
}
