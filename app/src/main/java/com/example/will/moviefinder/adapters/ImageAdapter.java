package com.example.will.moviefinder.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.will.moviefinder.R;
import com.example.will.moviefinder.objects.Details;
import com.squareup.picasso.Picasso;
import com.example.will.moviefinder.objects.Poster;

import java.util.List;

/**
 * Created by will on 9/13/2015.
 */
public class ImageAdapter extends ArrayAdapter<Details> {
    Context context;

    public ImageAdapter(Context context, int resourceId, //resourceId=your layout
                                 List<Details> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Details movie = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_item_poster, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.poster);
            holder.imageView.setAdjustViewBounds(true);


            Picasso.with(context).load(movie.getPosterUrl()).into(holder.imageView);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        return convertView;
    }
}
