package com.directions.route;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class XMLParser {

    protected static final String MARKERS = "markers";
    protected static final String MARKER = "marker";
    protected static final String LOG_TAG = "POSEIDON-Navigation";

    protected String mFeedUrl;
    private boolean mLocal;

    protected XMLParser(final String feedUrl, boolean local) {
        mLocal = local;
        mFeedUrl = feedUrl;

    }

    protected InputStream getInputStream() {
        try {
            if (mLocal) {
                File localFile = new File(mFeedUrl);
                return new FileInputStream(localFile);
            } else {
                return new URL(mFeedUrl).openConnection().getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }

    /**
     * Convert an inputstream to a string.
     *
     * @param input inputstream to convert.
     * @return a String of the inputstream.
     */

    protected static String convertStreamToString(final InputStream input) {
        if (input == null) return null;

        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final StringBuilder sBuf = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }
        return sBuf.toString();
    }
}