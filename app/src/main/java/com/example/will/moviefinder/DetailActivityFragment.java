package com.example.will.moviefinder;

import android.content.Intent;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        MovieDetails movieDetails;
        if (intent != null) {
            Bundle data = intent.getExtras();
            movieDetails =  data.getParcelable("details");

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

        return rootView;
    }

}
