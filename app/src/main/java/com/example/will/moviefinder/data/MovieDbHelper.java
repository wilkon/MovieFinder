package com.example.will.moviefinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
<<<<<<< HEAD
 * Created by will on 1/7/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

=======
 * Created by will on 1/24/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
>>>>>>> 43dcb4f... setting up content provider
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
<<<<<<< HEAD

=======
        final String SQL_CREATE_DETAILS_TABLE = "CREATE TABLE " + MoviesContract.DetailsEntry.TABLE_NAME + " (" +
                MoviesContract.DetailsEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesContract.DetailsEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL " +
                MoviesContract.DetailsEntry.COLUMN_TITLE + " TEXT NOT NULL " +
                MoviesContract.DetailsEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE + " INTEGER NOT NULL, " +
                MoviesContract.DetailsEntry.COLUMN_RATING + " REAL NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_DETAILS_TABLE);
>>>>>>> 43dcb4f... setting up content provider
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
<<<<<<< HEAD

=======
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.DetailsEntry.TABLE_NAME);
        onCreate(db);
>>>>>>> 43dcb4f... setting up content provider
    }
}
