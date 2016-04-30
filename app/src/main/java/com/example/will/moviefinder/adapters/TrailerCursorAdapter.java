package com.example.will.moviefinder.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.will.moviefinder.R;

import java.util.ArrayList;

/**
 * Created by will on 4/9/16.
 */
public class TrailerCursorAdapter extends CursorAdapter{
    ArrayList<String> sources = new ArrayList();

    public TrailerCursorAdapter(Context context,
                                Cursor c, int flags){
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        sources.clear();
        return LayoutInflater.from(context).inflate(R.layout.list_item_trailer, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView trailerUrl = (TextView) view.findViewById(R.id.trailer);
        String source = cursor.getString(cursor.getColumnIndex("trailer_link"));
        sources.add(source);
        Log.d("", "bind view called");
        trailerUrl.setText("Trailer");
    }

    public String getUrl(int position){
        return sources.get(position);
    }

}
