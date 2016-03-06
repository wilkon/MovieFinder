package com.example.will.moviefinder;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.will.moviefinder.adapters.ImageAdapter;
import com.example.will.moviefinder.adapters.ImageCursorAdapter;
import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.example.will.moviefinder.tasks.FetchPosterTask;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by will on 9/9/2015.
 */
public class PostersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String LOG_TAG = PostersFragment.class.getSimpleName();

    private static final int IMAGE_LOADER = 0;
    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;

    static final int COL_DETAIL_POSTER_URL = 4;


    ImageCursorAdapter imageCursorAdapter = null;
    ImageAdapter imageAdapter = null;
    ArrayList<MovieDetails> movies = new ArrayList<MovieDetails>();

    public PostersFragment(){

    }

    @Override
    public void onResume(){
        super.onResume();
        updatePosters();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        savedState.putParcelableArrayList("gridImages", imageAdapter.getValues());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(getContext());

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(getContext())
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getContext())
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
        super.onCreate(savedInstanceState);

//        if (savedInstanceState != null) {
//            String[] values = savedInstanceState.getStringArray("gridImages");
//            if (values != null) {
//                imageCursorAdapter = new ImageCursorAdapter(getActivity(), c)
//                imageAdapter = new ImageAdapter(
//                        getActivity(),
//                        R.layout.grid_item_poster,
//                        (ArrayList<MovieDetails>)savedInstanceState.get("gridImages"));
//            }
//        }
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        imageCursorAdapter = new ImageCursorAdapter(getActivity(), null, 0);
//        imageAdapter =
//                new ImageAdapter(
//                        getActivity(), // The current context (this activity)
//                        R.layout.grid_item_poster, // The name of the layout ID.
//                        movies);

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_posterlist);
        gridView.setAdapter(imageCursorAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetails movieId = imageCursorAdapter.getItem(position);

                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("details", movieId);
                startActivity(detailActivity);
            }
        });


        return rootView;

    }

    private void updatePosters(){
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = prefs.getString("sortBy", "popularity.desc");

            FetchPosterTask posterTask = new FetchPosterTask(imageAdapter, getActivity());
            posterTask.execute(sortBy).get(1000, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            Log.v(LOG_TAG, "updatePosters - Posters not updating properly");
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(), MoviesContract.DetailsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        imageCursorAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        imageCursorAdapter.swapCursor(null);
    }
}
