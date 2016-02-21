package com.example.will.moviefinder;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private static long uriId = 0;

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

            ((Button) rootView.findViewById(R.id.detail_mark_as_fav)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues favoritesEntry = new ContentValues();
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieDetails.get)

                    getActivity().getContentResolver().insert(MoviesContract.FavoritesEntry.CONTENT_URI, )
                }
            });
        }

        return rootView;
    }

}
