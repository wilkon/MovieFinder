package com.example.will.moviefinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by will on 1/7/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_DETAILS_TABLE = "CREATE TABLE " + MoviesContract.DetailsEntry.TABLE_NAME + " (" +
                MoviesContract.DetailsEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_RATING + " REAL NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_DETAILS_TABLE);

        //creating favorites movies db
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + MoviesContract.FavoritesEntry.TABLE_NAME + " (" +
            MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID +  " TEXT NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_RATING + " REAL NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_RUN_TIME + " TEXT, " +
            MoviesContract.FavoritesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
            MoviesContract.FavoritesEntry.COLUMN_POSTER_IMG + " BLOB " +
            ");";

        db.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.DetailsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + "movies");

    }
}
