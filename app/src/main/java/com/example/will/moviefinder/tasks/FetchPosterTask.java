package com.example.will.moviefinder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.helpers.AccessKeys;
import com.example.will.moviefinder.helpers.JsonHelper;
import com.example.will.moviefinder.objects.MovieDetails;
import com.example.will.moviefinder.tasks.util.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Vector;

/**
 * Created by will on 9/18/15.
 */
public class FetchPosterTask extends AsyncTask<String, Void, Void> {
    private final String IMAGE_RES = "w185";

    private String LOG_TAG = FetchPosterTask.class.getSimpleName();

    private final Context mContext;

    public FetchPosterTask(Context context){
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(Void v) {
//        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String sortBy = params[0];
        if (sortBy.equals("favorites")) {
            getMovieDetailsFromCursor();
            return null;
        }
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
            getMovieDetailsFromJson(sortedMovieJson);
        }catch(Exception e){
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it
        }
        return null;
    }
    private MovieDetails[] getMovieDetailsFromCursor(){

        Cursor cursor = mContext.getContentResolver().query(MoviesContract.FavoritesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        MovieDetails[] rMovies = new MovieDetails[cursor.getCount()];

        Vector<ContentValues> cvVector = new Vector <>(cursor.getCount());
        ContentValues map;
        if (cursor.moveToFirst()) {
            do{
                map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, map);
                cvVector.add(map);
            }while(cursor.moveToNext());
        }
        cursor.close();

        refreshCache(cvVector);

        return rMovies;
    }
    private void getMovieDetailsFromJson(String moviesString)
            throws Exception {
        final String KEY_RESULTS="results";
        final String KEY_ID="id";
        final String KEY_ORIGINAL_TITLE = "original_title";
        final String KEY_OVERVIEW = "overview";
        final String KEY_RELEASE_DATE = "release_date";
        final String KEY_POSTER_PATH = "poster_path";
        final String KEY_RATING = "vote_average";


        JSONObject moviesJson = new JSONObject(moviesString);
        JSONArray moviesArray = moviesJson.getJSONArray(KEY_RESULTS);

        Vector<ContentValues> cvVector = new Vector <>(moviesArray.length());

        for(int i =0; i < moviesArray.length(); i++){
            JSONObject movie = moviesArray.getJSONObject(i);
            String movieId = movie.getString(KEY_ID);

            String runTime = getRunTimeFor(movieId);

            String releaseDate = movie.getString(KEY_RELEASE_DATE);
            releaseDate = releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : "No Year";

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath(IMAGE_RES);

            ContentValues detailContents = new ContentValues();
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_MOVIE_ID, movie.getString(KEY_ID));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_TITLE, movie.getString(KEY_ORIGINAL_TITLE));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_OVERVIEW, movie.getString(KEY_OVERVIEW));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE, releaseDate);
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_RATING, String.valueOf(movie.getDouble(KEY_RATING)));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_POSTER, builder.build().toString() + movie.getString(KEY_POSTER_PATH));
            detailContents.put(MoviesContract.DetailsEntry.COLUMN_RUN_TIME, runTime);
            cvVector.add(detailContents);
        }

        // add to database
        if ( cvVector.size() > 0 ) {
            refreshCache(cvVector);
        }
    }

    private String getRunTimeFor(String movieId) throws Exception{
        final String KEY_RUN_TIME = "runtime";
        URL url = UrlBuilder.getMovieUrl(movieId, "");

        String movieDetailsStr = JsonHelper.getString(url);

        Log.v(LOG_TAG, "Movie Specific JSON String" + movieDetailsStr);
        return new JSONObject(movieDetailsStr).getString(KEY_RUN_TIME);
    }

    private void refreshCache(Vector<ContentValues> cvVector){
        ContentValues[] cvArray = new ContentValues[cvVector.size()];
        cvVector.toArray(cvArray);

        int i = mContext.getContentResolver().delete(MoviesContract.DetailsEntry
                .CONTENT_URI, null, null);
        Log.d(LOG_TAG, "rows deleted from details table " + i);

        int inserted = mContext.getContentResolver().bulkInsert(MoviesContract.DetailsEntry
                .CONTENT_URI, cvArray);
        Log.d(LOG_TAG, "rows inserted into details table " + inserted);
    }
}