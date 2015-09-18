package com.example.will.moviefinder.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.will.moviefinder.R;
import com.example.will.moviefinder.objects.MovieDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by will on 9/13/2015.
 */
public class ImageAdapter extends ArrayAdapter<MovieDetails> {
    Context context;

    public ImageAdapter(Context context, int resourceId, //resourceId=your layout
                                 List<MovieDetails> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        MovieDetails movie = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.grid_item_poster, null);
        holder = new ViewHolder();
        holder.imageView = (ImageView) convertView.findViewById(R.id.poster);
        holder.imageView.setAdjustViewBounds(true);
        Picasso.with(context).load(movie.getPosterUrl()).into((ImageView) convertView.findViewById(R.id.poster));

        convertView.setTag(holder);

        return convertView;
    }

}
