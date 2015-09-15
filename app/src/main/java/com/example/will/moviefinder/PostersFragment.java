package com.example.will.moviefinder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.will.moviefinder.adapters.ImageAdapter;
import com.example.will.moviefinder.helpers.AccessKeys;
import com.example.will.moviefinder.objects.Details;
import com.example.will.moviefinder.objects.Poster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 9/9/2015.
 */
public class PostersFragment extends Fragment {

    ImageAdapter imageAdapter;
    private final String IMAGE_RES = "w185";

    public PostersFragment(){

    }

    @Override
    public void onStart(){
        super.onStart();
        updatePosters();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        List<Details> movies = new ArrayList<Details>();

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        imageAdapter =
                new ImageAdapter(
                        getActivity(), // The current context (this activity)
                        R.layout.grid_item_poster, // The name of the layout ID.
                        movies);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_posterlist);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Details movieId = imageAdapter.getItem(position);

                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("details", movieId);
                startActivity(detailActivity);
            }
        });


        return rootView;

    }

    private void updatePosters(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String postalCode = prefs.getString("location", "94043");

        FetchPosterTask posterTask = new FetchPosterTask();
        posterTask.execute();
    }

    private class FetchPosterTask extends AsyncTask<String, Void, Details[]> {

        private String LOG_TAG = FetchPosterTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Details[] movieDetails) {
            if(movieDetails != null){
                imageAdapter.clear();
                for(Details movieDetail : movieDetails){
                    imageAdapter.add(movieDetail);
                }
            }
        }

        @Override
        protected Details[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

           try{
               //get list of top movies
               Uri.Builder topMoviesBuilder = new Uri.Builder();
               topMoviesBuilder.scheme("http")
                       .authority("api.themoviedb.org")
                       .appendPath("3")
                       .appendPath("discover")
                       .appendPath("movie")
                       .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey())
                       .appendQueryParameter("sort_by", "popularity.desc");
               String builderUrl = topMoviesBuilder.build().toString();
               URL url = new URL(builderUrl);

               // Create the request to MovieDB, and open the connection
               urlConnection = (HttpURLConnection) url.openConnection();
               urlConnection.setRequestMethod("GET");
               urlConnection.connect();


               // Read the input stream into a String
               InputStream inputStream = urlConnection.getInputStream();
               StringBuffer buffer = new StringBuffer();
               if (inputStream == null) {
                   // Nothing to do.
                   return null;
               }
               reader = new BufferedReader(new InputStreamReader(inputStream));
               String line;
               while ((line = reader.readLine()) != null) {
                   // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                   // But it does make debugging a *lot* easier if you print out the completed
                   // buffer for debugging.
                   buffer.append(line + "\n");
               }
                return getPostersFromJson(buffer.toString());
           }catch(Exception e){
               Log.e("PlaceholderFragment", "Error ", e);
               // If the code didn't successfully get the weather data, there's no point in attemping
               // to parse it.
               return null;
           }
        }
        private Details[] getPostersFromJson(String moviesString)
                throws Exception {
            final String KEY_RESULTS="results";
            final String KEY_ID="id";
            final String KEY_ORIGINAL_TLTLE = "original_title";
            final String KEY_OVERVIEW = "overview";
            final String KEY_RUN_TIME = "runtime";
            final String KEY_RELEASE_DATE = "release_date";
            final String KEY_POSTER_PATH = "poster_path";
            final String KEY_RATING = "vote_average";


            JSONObject moviesJson = new JSONObject(moviesString);
            JSONArray moviesArray = moviesJson.getJSONArray(KEY_RESULTS);

            Details[] movieDetails = new Details[moviesArray.length()];

            for(int i =0; i < moviesArray.length(); i++){
                JSONObject movie = moviesArray.getJSONObject(i);
                String id = movie.getString(KEY_ID);

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("image.tmdb.org")
                        .appendPath("t")
                        .appendPath("p")
                        .appendPath(IMAGE_RES);
                movieDetails[i] =  new Details(
                        movie.getString(KEY_ORIGINAL_TLTLE),
                        movie.getString(KEY_OVERVIEW),
                        movie.getString(KEY_RELEASE_DATE),
                        getRunTime(id),
                        builder.build().toString() + movie.getString(KEY_POSTER_PATH),
                        movie.getString(KEY_RATING)
                );
            }
            return movieDetails;
        }
        private String getRunTime(String movieId) throws Exception{
            final String KEY_RUN_TIME = "runtime";

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieDetailsStr = null;
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(movieId)
                    .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey());
            URL url = new URL(builder.build().toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));


            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieDetailsStr = buffer.toString();
            Log.v(LOG_TAG, "forecast JSON String" + movieDetailsStr);
            return new JSONObject(movieDetailsStr).getString(KEY_RUN_TIME);
        }
    }

}
