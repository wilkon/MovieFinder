package com.example.will.moviefinder.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.will.moviefinder.R;
import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by will on 3/3/16.
 */
public class ImageCursorAdapter extends CursorAdapter{
    Context context;
    private ArrayList<MovieDetails> movies;

    public ImageCursorAdapter(Context context,
                              Cursor c, int flags, ArrayList<MovieDetails> items) {
        super(context, c, flags);
        this.context = context;
        this.movies = items;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View returnView = new ImageView(context);
        ViewHolder holder;
        MovieDetails movie = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        returnView = mInflater.inflate(R.layout.grid_item_poster, null);
        holder = new ViewHolder();
        holder.imageView = (ImageView) returnView.findViewById(R.id.poster);
        holder.imageView.setAdjustViewBounds(true);
        Picasso.with(context).load(movie.getPosterUrl()).into((ImageView) returnView.findViewById(R.id.poster));

        returnView.setTag(holder);
        return returnView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;

    }
}
