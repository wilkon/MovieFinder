package com.example.will.moviefinder;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.will.moviefinder.adapters.ImageAdapter;
import com.example.will.moviefinder.objects.MovieDetails;
import com.example.will.moviefinder.tasks.FetchPosterTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by will on 9/9/2015.
 */
public class PostersFragment extends Fragment {
    private String LOG_TAG = PostersFragment.class.getSimpleName();

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
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String[] values = savedInstanceState.getStringArray("gridImages");
            if (values != null) {
                imageAdapter = new ImageAdapter(
                        getActivity(),
                        R.layout.grid_item_poster,
                        (ArrayList<MovieDetails>)savedInstanceState.get("gridImages"));
            }
        }
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
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortBy = prefs.getString("sortBy", "popularity.desc");

            FetchPosterTask posterTask = new FetchPosterTask(imageAdapter);
            posterTask.execute(sortBy).get(1000, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            Log.v(LOG_TAG, "updatePosters - Posters not updating properly");
        }
    }

}
