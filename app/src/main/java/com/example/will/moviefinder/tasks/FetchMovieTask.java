package com.example.will.moviefinder.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.helpers.JsonHelper;
import com.example.will.moviefinder.tasks.util.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Vector;

/**
 * Created by will on 3/13/16.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {
    Context mContext;
    String movieId;


    private String LOG_TAG = FetchMovieTask.class.getSimpleName();

    public FetchMovieTask(Context context, String movieId){
        this.mContext = context;
        this.movieId = movieId;
    }

    @Override
    protected Void doInBackground(String... params) {
        try{
            String[] trailers = getTrailersFor(movieId);

            Log.d(LOG_TAG, "*******"+trailers.length + " TRAILERS");


//            String[] trailers = getTrailersFor(movieId);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    private String[] getReviewsFor(String movieId) throws Exception{
        URL url = UrlBuilder.getMovieUrl(movieId, "reviews");

        JSONObject reviewsJson = new JSONObject(JsonHelper.getString(url));
        JSONArray reviewsArray = reviewsJson.getJSONArray("results");
        String[] reviews = new String[reviewsArray.length()];

        for(int i =0; i < reviewsArray.length(); i++){
            reviews[i] = reviewsArray.getJSONObject(i).getString("content");
        }
        return reviews;
    }

    private String[] getTrailersFor(String movieId) throws Exception{

        int _deleted = mContext.getContentResolver().delete(MoviesContract.TrailersEntry
                .CONTENT_URI, null, null);
        Log.d(LOG_TAG, "rows deleted from details table " + _deleted);

        URL url = UrlBuilder.getMovieUrl(movieId, "trailers");

        JSONObject reviewsJson = new JSONObject(JsonHelper.getString(url));
        JSONArray reviewsArray = reviewsJson.getJSONArray("youtube");
        int trailersCount = reviewsArray.length();

        String[] trailers = new String[trailersCount];

        Vector<ContentValues> cvVector = new Vector<>(trailersCount);

        for(int i =0; i < trailersCount; i++){
            String youtubeSource = reviewsArray.getJSONObject(i).getString("source");
            trailers[i] = reviewsArray.getJSONObject(i).getString("source");

            ContentValues trailer = new ContentValues();
            trailer.put(MoviesContract.TrailersEntry.COLUMN_CONTENT, youtubeSource);
            trailer.put(MoviesContract.TrailersEntry.COLUMN_MOVIE_ID, movieId);
            cvVector.add(trailer);
        }

        ContentValues[] cvArray = new ContentValues[cvVector.size()];
        cvVector.toArray(cvArray);

        int inserted = mContext.getContentResolver().bulkInsert(MoviesContract.TrailersEntry
                .CONTENT_URI, cvArray);

        Log.d(LOG_TAG, "getTrailersFor: " + inserted);

        return trailers;
    }
}
