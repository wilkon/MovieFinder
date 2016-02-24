package com.example.will.moviefinder;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static long uriId = 0;
    private final String MINI_IMAGE_RES = "w185";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final MovieDetails movieDetails;
        if (intent != null) {
            Bundle data = intent.getExtras();
            movieDetails =  data.getParcelable("details");

            ((TextView) rootView.findViewById(R.id.detail_movie_title))
                    .setText(movieDetails.getOrigTitle());

            ImageView posterImageView = ((ImageView) rootView.findViewById(R.id.detail_poster));
            posterImageView.setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(movieDetails.getPosterUrl()).into(posterImageView);

            String releaseDate = movieDetails.getReleaseDate();
            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(releaseDate.length() >= 4 ? releaseDate.substring(0, 4) : "No Year");

            ((TextView) rootView.findViewById(R.id.detail_run_time))
                    .setText(movieDetails.getRunTime()+"min");

            ((TextView) rootView.findViewById(R.id.detail_rating))
                    .setText(movieDetails.getRating() +"/10");

            ((TextView) rootView.findViewById(R.id.detail_description))
                    .setText(movieDetails.getDesc());

            rootView.findViewById(R.id.detail_mark_as_fav).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues favoritesEntry = new ContentValues();
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_TITLE, movieDetails.getOrigTitle());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_OVERVIEW, movieDetails.getDesc());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE, movieDetails.getReleaseDate());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RATING, movieDetails.getReleaseDate());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RUN_TIME, movieDetails.getRunTime());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_POSTER, movieDetails.getPosterUrl());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_POSTER_IMG, "");
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieDetails.getMovieId());

                    getActivity().getContentResolver().insert(MoviesContract.FavoritesEntry.CONTENT_URI, favoritesEntry);
                }
            });
        }

        return rootView;
    }

    public String getImageString(String imagePath){
        String rString = null;
        try{
            Bitmap image = Picasso.with(getActivity()).load(imagePath).get();
            int bytes = image.getByteCount();

            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            image.copyPixelsFromBuffer(buffer);

            rString = new String(buffer.array(), "UTF-8");
        }catch(IOException e){
            e.printStackTrace();
        }
        return rString;
    }

}
