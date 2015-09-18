package com.example.will.moviefinder.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by will on 9/18/2015.
 */
public class JsonHelper {

    public static  String getString(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        return buffer.toString();
    }
}
