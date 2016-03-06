package com.example.will.moviefinder.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.will.moviefinder.PostersFragment;
import com.example.will.moviefinder.R;
import com.example.will.moviefinder.data.MoviesContract;
import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by will on 3/3/16.
 */
public class ImageCursorAdapter extends CursorAdapter {
    Context context;

    private static final int VIEW_MOVIE_POSTERS = 0;
    static final int COL_DETAIL_POSTER_URL = 4;

    public ImageCursorAdapter(Context context,
                              Cursor c, int flags) {
        super(context, c, flags);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;

        public ViewHolder(View view) {
            this.imageView = (ImageView)view.findViewById(R.id.poster);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_poster, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.imageView.setAdjustViewBounds(true);
        String posterUrl = cursor.getString(COL_DETAIL_POSTER_URL);
        Picasso.with(context).load(posterUrl).into(viewHolder.imageView);
    }

    @Override
    public MovieDetails getItem(int position) {
        return null;
    }
}
