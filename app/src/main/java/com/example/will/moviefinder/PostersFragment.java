package com.example.will.moviefinder;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.will.moviefinder.adapters.ImageCursorAdapter;
import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.example.will.moviefinder.tasks.FetchPosterTask;
import com.facebook.stetho.Stetho;

import java.util.concurrent.TimeUnit;

/**
 * Created by will on 9/9/2015.
 */
public class PostersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String LOG_TAG = PostersFragment.class.getSimpleName();

    private static final int IMAGE_LOADER = 0;
    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private static String mSort = "";

    ImageCursorAdapter imageCursorAdapter = null;

    private static final String[] DETAIL_COLUMNS= new String[]{
        MoviesContract.DetailsEntry._ID,
        MoviesContract.DetailsEntry.COLUMN_MOVIE_ID,
        MoviesContract.DetailsEntry.COLUMN_TITLE,
        MoviesContract.DetailsEntry.COLUMN_OVERVIEW,
        MoviesContract.DetailsEntry.COLUMN_POSTER,
        MoviesContract.DetailsEntry.COLUMN_RELEASE_DATE,
        MoviesContract.DetailsEntry.COLUMN_RATING,
        MoviesContract.DetailsEntry.COLUMN_RUN_TIME
    };

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_MOVIE_ID = 1;
    private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_OVERVIEW = 3;
    private static final int COLUMN_POSTER = 4;
    private static final int COLUMN_RELEASE_DATE = 5;
    private static final int COLUMN_RATING = 6;
    private static final int COLUMN_RUN_TIME = 7;

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    @Override
    public void onResume(){
        super.onResume();
        updatePosters();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
//        savedState.putParcelableArrayList("gridImages", imageAdapter.getValues());
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

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.gridview_posterlist);
        mGridView.setAdapter(imageCursorAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long movieId = imageCursorAdapter.getItemId(position);
                MovieDetails details = null;
                Cursor cursor = getContext().getContentResolver().query(
                        MoviesContract.DetailsEntry.CONTENT_URI,
                        DETAIL_COLUMNS,
                        "_id=?",
                        new String[]{String.valueOf(movieId)},
                        null
                );

                if(cursor.moveToFirst()){
                    details = new MovieDetails(
                            cursor.getString(COLUMN_TITLE),
                            cursor.getString(COLUMN_OVERVIEW),
                            cursor.getString(COLUMN_RELEASE_DATE),
                            cursor.getString(COLUMN_RUN_TIME),
                            cursor.getString(COLUMN_POSTER),
                            cursor.getString(COLUMN_RATING),
                            null,
                            cursor.getString(COLUMN_MOVIE_ID)
                    );
                }
                cursor.close();

                Intent detailActivity = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("details", details);
                startActivity(detailActivity);

            }
        });


        return rootView;

    }

    private void updatePosters(){
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = prefs.getString("sortBy", "popularity.desc");
            if(mSort.equals("") || mSort != sortBy){
                mSort = sortBy;
                FetchPosterTask posterTask = new FetchPosterTask(getActivity());
                posterTask.execute(sortBy).get(1000, TimeUnit.MILLISECONDS);
            }
        }catch(Exception e){
            Log.v(LOG_TAG, "updatePosters - Posters not updating properly");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(IMAGE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this.getActivity(), MoviesContract.DetailsEntry.CONTENT_URI,
                DETAIL_COLUMNS,
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
