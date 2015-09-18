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
import com.example.will.moviefinder.helpers.JsonHelper;
import com.example.will.moviefinder.objects.MovieDetails;

import org.json.JSONArray;
import org.json.JSONObject;

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
        List<MovieDetails> movies = new ArrayList<MovieDetails>();

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
                MovieDetails movieId = imageAdapter.getItem(position);

                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("details", movieId);
                startActivity(detailActivity);
            }
        });


        return rootView;

    }

    private void updatePosters(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = prefs.getString("sortBy", "popularity.desc");

        FetchPosterTask posterTask = new FetchPosterTask();
        posterTask.execute(sortBy);
    }

    private class FetchPosterTask extends AsyncTask<String, Void, MovieDetails[]> {

        private String LOG_TAG = FetchPosterTask.class.getSimpleName();

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
                String id = movie.getString(KEY_ID);

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
                        getRunTime(id),
                        builder.build().toString() + movie.getString(KEY_POSTER_PATH),
                        movie.getString(KEY_RATING)
                );
            }
            return movieDetails;
        }

        private String getRunTime(String movieId) throws Exception{
            final String KEY_RUN_TIME = "runtime";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(movieId)
                    .appendQueryParameter("api_key", AccessKeys.getMoviedbApiKey());
            URL url = new URL(builder.build().toString());

            String movieDetailsStr = JsonHelper.getString(url);

            Log.v(LOG_TAG, "Movie Specific JSON String" + movieDetailsStr);
            return new JSONObject(movieDetailsStr).getString(KEY_RUN_TIME);
        }
    }


}
