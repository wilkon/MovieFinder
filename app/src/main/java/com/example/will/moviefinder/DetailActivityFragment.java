package com.example.will.moviefinder;
;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.will.moviefinder.adapters.TrailerCursorAdapter;
import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.example.will.moviefinder.tasks.FetchMovieTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    private ListView mListView;
    private int mPosition = ListView.INVALID_POSITION;
    private static final int TRAILER_LOADER = 0;

    TrailerCursorAdapter trailerCursorAdapter = null;

    private static final String[] TRAILER_COLUMNS = new String[]{
            MoviesContract.TrailersEntry._ID,
            MoviesContract.TrailersEntry.COLUMN_MOVIE_ID,
            MoviesContract.TrailersEntry.COLUMN_CONTENT
    };

    public DetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
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

            //get reviews to display, store in case of favorites implementation
            getTrailers(movieDetails.getMovieId());

            ((TextView) rootView.findViewById(R.id.detail_movie_title))
                    .setText(movieDetails.getOrigTitle());

            ImageView posterImageView = ((ImageView) rootView.findViewById(R.id.detail_poster));
            posterImageView.setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(movieDetails.getPosterUrl()).into(posterImageView);

            ((TextView) rootView.findViewById(R.id.detail_release_date))
                    .setText(movieDetails.getReleaseDate());

            ((TextView) rootView.findViewById(R.id.detail_run_time))
                    .setText(movieDetails.getRunTime() + "min");

            ((TextView) rootView.findViewById(R.id.detail_rating))
                    .setText(movieDetails.getRating() + "/10");

            ((TextView) rootView.findViewById(R.id.detail_description))
                    .setText(movieDetails.getDesc());

            trailerCursorAdapter = new TrailerCursorAdapter(getActivity(), null, 0);



            mListView = (ListView) rootView.findViewById(R.id.detail_trailers);
            mListView.setAdapter(trailerCursorAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(LOG_TAG, "YOUTUBE SOURCE: " + trailerCursorAdapter.getUrl(position));
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerCursorAdapter.getUrl(position)));
                        startActivity(intent);
                    } catch (Exception e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/v/" + id));
                        startActivity(intent);
                    }
                }
            });
            Log.d(LOG_TAG, "!@)(!#*)@(*#!#)(* adapter count: "+ mListView.getAdapter().getCount());

            rootView.findViewById(R.id.detail_reviews).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });

            rootView.findViewById(R.id.detail_mark_as_fav).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues favoritesEntry = new ContentValues();
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_TITLE, movieDetails.getOrigTitle());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_OVERVIEW, movieDetails.getDesc());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE, movieDetails.getReleaseDate());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RATING, movieDetails.getRating());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_RUN_TIME, movieDetails.getRunTime());
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_POSTER, movieDetails.getPosterUrl());
//                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_POSTER_IMG, "");
                    favoritesEntry.put(MoviesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieDetails.getMovieId());
                    getTrailers(movieDetails.getMovieId());

                    Uri uri = getActivity().getContentResolver().insert(MoviesContract.FavoritesEntry.CONTENT_URI, favoritesEntry);

                    Log.d(LOG_TAG, uri.toString());
                }
            });


            // call async task to populate trailers
            // display trailers on listview
        }

        return rootView;
    }

    private void getTrailers(String movieId){

        FetchMovieTask movieTask = new FetchMovieTask(getActivity(), movieId);
        movieTask.execute();

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this.getActivity(), MoviesContract.TrailersEntry.CONTENT_URI,
                TRAILER_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        trailerCursorAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mListView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        trailerCursorAdapter.swapCursor(null);
    }
}
