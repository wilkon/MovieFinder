package com.example.will.moviefinder.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

/**
 * Created by will on 9/18/15.
 */
public class FetchPosterTask extends AsyncTask<String, Void, MovieDetails[]> {
    private final String IMAGE_RES = "w185";
    private ImageAdapter imageAdapter;

    private String LOG_TAG = FetchPosterTask.class.getSimpleName();

    private final Context mContext;

    public FetchPosterTask(ImageAdapter imageAdapter, Context context){
        this.imageAdapter = imageAdapter;
        this.mContext = context;
    }

    long addDetail(String movieId, String title, String overview, String releaseDate, String posterPath, String rating){
        long returnId;

        Cursor detailsCursor = mContext.getContentResolver().query(
                MoviesContract.DetailsEntry.CONTENT_URI,
                new String[]{MoviesContract.DetailsEntry._ID},
                MoviesContract.DetailsEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null
        );
        if(detailsCursor.moveToFirst()){
            int movieIdIndex = detailsCursor.getColumnIndex(MoviesContract.DetailsEntry._ID);
            returnId = detailsCursor.getLong(movieIdIndex);
        }else {
            ContentValues detailValues = new ContentValues();

            detailValues.put(MoviesContract.DetailsEntry.COLUMN_TITLE, title);
            detailValues.put(MoviesContract.DetailsEntry.COLUMN_OVERVIEW, overview);
            detailValues.put(MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, releaseDate);
            detailValues.put(MoviesContract.DetailsEntry.COLUMN_POSTER, posterPath);
            detailValues.put(MoviesContract.DetailsEntry.COLUMN_RATING, rating);

            Uri insertedUri = mContext.getContentResolver().insert(
                    MoviesContract.DetailsEntry.CONTENT_URI,
                    detailValues
            );

            returnId = ContentUris.parseId(insertedUri);
        }
        detailsCursor.close();

        return returnId;
    }

    @Override
    protected void onPostExecute(MovieDetails[] movieDetails) {
        if(movieDetails != null){
            imageAdapter.clear();
            imageAdapter.addAll(movieDetails);
        }
    }

    @Override
    protected MovieDetails[] doInBackground(String... params) {

        try{
            //get list of top movies
            Uri.Builder topMoviesBuilder = new Uri.Builder();
            topMoviesBuilder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey())
                    .appendQueryParameter("sort_by", params[0]);
            String builderUrl = topMoviesBuilder.build().toString();
            URL url = new URL(builderUrl);

            String sortedMovieJson = JsonHelper.getString(url);

            return getMovieDetailsFromJson(sortedMovieJson);
        }catch(Exception e){
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        }
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

        for(int i =0; i < moviesArray.length(); i++){
            JSONObject movie = moviesArray.getJSONObject(i);
            String runTime = getRunTime(movie.getString(KEY_ID));

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
                    runTime,
                    builder.build().toString() + movie.getString(KEY_POSTER_PATH),
                    movie.getString(KEY_RATING)
            );
        }
        return movieDetails;
    }
}