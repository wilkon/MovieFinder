package com.example.will.moviefinder.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by will on 1/24/2016.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.will.moviefinder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DETAILS = "details";
    public static final String PATH_FAVORITES = "favorites";
    public static final String PATH_MOVIES = "movies";

    public static final class DetailsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DETAILS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;



        // Table name
        public static final String TABLE_NAME = "details";

        // Columnsd
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "detail_title";
        public static final String COLUMN_RELEASE_DATE = "detail_release_date";
        public static final String COLUMN_RATING = "detail_rating";
        public static final String COLUMN_OVERVIEW = "detail_overview";
        public static final String COLUMN_POSTER = "detail_poster";

        public static Uri buildDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class FavoritesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        // Table name
        public static final String TABLE_NAME = "favorites";

        // Columns
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "detail_title";
        public static final String COLUMN_RELEASE_DATE = "detail_release_date";
        public static final String COLUMN_RATING = "detail_rating";
        public static final String COLUMN_OVERVIEW = "detail_overview";
        public static final String COLUMN_RUN_TIME = "detail_run_time";
        public static final String COLUMN_POSTER = "detail_poster";
        public static final String COLUMN_POSTER_IMG = "detail_poster_img";

        public static Uri buildFavoritesUri(long id ){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
