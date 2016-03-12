package com.example.will.moviefinder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.will.moviefinder.adapters.ImageAdapter;
import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.helpers.AccessKeys;
import com.example.will.moviefinder.helpers.JsonHelper;
import com.example.will.moviefinder.objects.MovieDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Vector;

/**
 * Created by will on 9/18/15.
 */
public class FetchPosterTask extends AsyncTask<String, Void, Void> {
    private final String IMAGE_RES = "w185";
    private ImageAdapter imageAdapter;

    private String LOG_TAG = FetchPosterTask.class.getSimpleName();

    private final Context mContext;
    private static boolean hasCache = false;

    public FetchPosterTask(ImageAdapter imageAdapter, Context context){
        this.imageAdapter = imageAdapter;
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(Void v) {
//        if(movieDetails != null){
//            imageAdapter.clear();
//            imageAdapter.addAll(movieDetails);
//        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String sortBy = params[0];
        try {
            if (sortBy.equals("favorites")) {
                getMovieDetailsFromCursor();
            }
            //get list of top movies
            Uri.Builder topMoviesBuilder = new Uri.Builder();
            topMoviesBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey())
                    .appendQueryParameter("sort_by", sortBy);
            String builderUrl = topMoviesBuilder.build().toString();
            URL url = new URL(builderUrl);

            String sortedMovieJson = JsonHelper.getString(url);
            MovieDetails[] returnObject = getMovieDetailsFromJson(sortedMovieJson);
            Log.i(LOG_TAG, "items in movieDetails array " + returnObject.length);
        }catch(Exception e){
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it
        }
        return null;
    }
    private MovieDetails[] getMovieDetailsFromCursor(){

        final int movie_id = 0;
        final int detail_title = 1;
        final int detail_overview = 2;
        final int detail_poster = 3;
        final int detail_release_date = 4;
        final int detail_rating = 5;

        Cursor cursor = mContext.getContentResolver().query(MoviesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        MovieDetails[] rMovies = new MovieDetails[cursor.getCount()];

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath(IMAGE_RES);
        int currIndex = 0;
        while(cursor.moveToNext()){
            rMovies[currIndex] =  new MovieDetails(
                    cursor.getString(detail_title),
                    cursor.getString(detail_overview),
                    cursor.getString(detail_release_date),
                    "",
                    builder.build().toString() + cursor.getString(detail_poster),
                    cursor.getString(detail_rating),
                    null,
                    cursor.getString(movie_id));
            currIndex++;
        }
        cursor.close();
        return rMovies;
    }
    private MovieDetails[] getMovieDetailsFromJson(String moviesString)
            throws Exception {
        final String KEY_RESULTS="results";
        final String KEY_ID="id";
        final String KEY_ORIGINAL_TLTLE = "original_title";
        final String KEY_OVERVIEW = "overview";
        final String KEY_RELEASE_DATE = "release_date";
        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_RATING = "vote_average";


        JSONObject moviesJson = new JSONObject(moviesString);
        JSONArray moviesArray = moviesJson.getJSONArray(KEY_RESULTS);

        MovieDetails[] movieDetails = new MovieDetails[moviesArray.length()];

        Vector<ContentValues> cvVector = new Vector <>(moviesArray.length());

        for(int i =0; i < moviesArray.length(); i++){
            JSONObject movie = moviesArray.getJSONObject(i);
            //String runTime = getRunTime(movie.getString(KEY_ID));

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath(IMAGE_RES);
            movieDetails[i] =  new MovieDetails(
                    movie.getString(KEY_ORIGINAL_TLTLE),
                    movie.getString(KEY_OVERVIEW),
                    movie.getString(KEY_RELEASE_DATE),
                    "",
                    builder.build().toString() + movie.getString(KEY_POSTER_PATH),
                    movie.getString(KEY_RATING),
                    null,
                    movie.getString(KEY_ID)
            );

            ContentValues detailContents = new ContentValues();
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_MOVIE_ID, movie.getString(KEY_ID));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_TITLE, movie.getString(KEY_ORIGINAL_TLTLE));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_OVERVIEW, movie.getString(KEY_OVERVIEW));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, movie.getString(KEY_RELEASE_DATE));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_RATING, movie.getString(KEY_RATING));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_POSTER, builder.build().toString() + movie.getString(KEY_POSTER_PATH));
            cvVector.add(detailContents);

        }

        int inserted = 0;
        // add to database
        if ( cvVector.size() > 0 ) {
            int i = mContext.getContentResolver().delete(MoviesContract.DetailsEntry.CONTENT_URI, null, null);
            Log.d(LOG_TAG, "rows deleted from details table " + i);
            ContentValues[] cvArray = new ContentValues[cvVector.size()];
            cvVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MoviesContract.DetailsEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "FetchPosterTask Complete. " + inserted + " Inserted");

        return movieDetails;
    }
}