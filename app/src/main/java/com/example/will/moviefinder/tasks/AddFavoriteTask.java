package com.example.will.moviefinder.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;

/**
 * Created by will on 1/31/16.
 */
public class AddFavoriteTask extends AsyncTask<String, Void, Void>{


    private String LOG_TAG = AddFavoriteTask.class.getSimpleName();

    private final Context mContext;
    private MovieDetails fMovieDetails;

    public AddFavoriteTask(MovieDetails fMovieDetails, Context context){
        this.fMovieDetails = fMovieDetails;
        this.mContext = context;
    }


    public long addFavorite(MovieDetails fMovie){
        long returnId;

        Cursor favoritesCursor = mContext.getContentResolver().query(
                MoviesContract.FavoritesEntry.CONTENT_URI,
                new String[]{MoviesContract.FavoritesEntry._ID},
                MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{fMovie.getMovieId()},
                null
        );
        if(favoritesCursor.moveToFirst()){
            int movieIdIndex = favoritesCursor.getColumnIndex(MoviesContract.FavoritesEntry._ID);
            returnId = favoritesCursor.getLong(movieIdIndex);
        }else {
            ContentValues detailValues = new ContentValues();
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, fMovie.getMovieId());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_TITLE, fMovie.getOrigTitle());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_OVERVIEW, fMovie.getDesc());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE, fMovie.getReleaseDate());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_RATING, fMovie.getRating());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_POSTER, fMovie.getPosterUrl());
            detailValues.put(MoviesContract.FavoritesEntry.COLUMN_POSTER_IMG, fMovie.getPoster_img());

            Uri insertedUri = mContext.getContentResolver().insert(
                    MoviesContract.DetailsEntry.CONTENT_URI,
                    detailValues
            );

            returnId = ContentUris.parseId(insertedUri);
        }
        favoritesCursor.close();

        return returnId;
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
