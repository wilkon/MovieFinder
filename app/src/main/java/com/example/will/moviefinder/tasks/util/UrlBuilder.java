package com.example.will.moviefinder.tasks.util;

import android.net.Uri;

import com.example.will.moviefinder.helpers.AccessKeys;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by will on 3/13/16.
 */
public class UrlBuilder {
    public static URL getMovieUrl(String movieId, String... appendParams) throws MalformedURLException {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieId)
                .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey());
        if(!appendParams[0].equals("")){
            for(String param : appendParams){
                builder.appendPath(param);
            }
        }
        return new URL(builder.build().toString());
    }
}
