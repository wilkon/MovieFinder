package com.example.will.moviefinder;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.will.moviefinder.helpers.AccessKeys;
import com.example.will.moviefinder.objects.Details;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    Details movieDetails;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        Details movieDetails;
        try{
            if (intent != null) {
                Bundle data = intent.getExtras();
                movieDetails = (Details) data.getParcelable("details");

                ((TextView) rootView.findViewById(R.id.detail_movie_title))
                        .setText(movieDetails.getOrigTitle());

                ImageView posterImageView = ((ImageView) rootView.findViewById(R.id.detail_poster));
                posterImageView.setAdjustViewBounds(true);
                Picasso.with(getActivity()).load(movieDetails.getPosterUrl()).into(posterImageView);

                ((TextView) rootView.findViewById(R.id.detail_release_date))
                        .setText(movieDetails.getReleaseDate().substring(0,4));

                ((TextView) rootView.findViewById(R.id.detail_run_time))
                        .setText(movieDetails.getRunTime()+"min");

                ((TextView) rootView.findViewById(R.id.detail_rating))
                        .setText(movieDetails.getRating() +"/10");

                ((TextView) rootView.findViewById(R.id.detail_description))
                        .setText(movieDetails.getDesc());
            }


        }catch(Exception e){

        }

        return rootView;
    }


    private class FetchDetailsTask extends AsyncTask<String, Void, Details> {


        @Override
        protected Details doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieDetailsStr = null;

            try {
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath(params[0])
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

                return movieDetails = getMovieDetailsFromJson(movieDetailsStr);
            } catch (Exception e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
            }
            return null;
        }

        private Details getMovieDetailsFromJson(String jsonString) throws JSONException {

            final String KEY_ORIGINAL_TLTLE = "original_title";
            final String KEY_OVERVIEW = "overview";
            final String KEY_RUN_TIME = "runtime";
            final String KEY_RELEASE_DATE = "release_date";
            final String KEY_POSTER_PATH = "poster_path";
            final String KEY_RATING = "vote_average";

            JSONObject movieJson = new JSONObject(jsonString);
            return new Details(
                    movieJson.getString(KEY_ORIGINAL_TLTLE),
                    movieJson.getString(KEY_OVERVIEW),
                    movieJson.getString(KEY_RELEASE_DATE),
                    movieJson.getString(KEY_RUN_TIME),
                    movieJson.getString(KEY_POSTER_PATH),
                    movieJson.getString(KEY_RATING)
            );
        }
    }


}
