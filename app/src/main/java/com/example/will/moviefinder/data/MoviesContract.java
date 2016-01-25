package com.example.will.moviefinder.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by will on 1/24/2016.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.will.moviefinder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DETAILS = "details";

    public static final class DetailsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DETAILS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DETAILS;



        // Table name
        public static final String TABLE_NAME = "details";

        // Columns
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

    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }
}
