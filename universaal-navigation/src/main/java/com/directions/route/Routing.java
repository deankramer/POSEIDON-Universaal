package com.directions.route;

/**
 * Async Task to access the Google Direction API and return the routing data
 * which is then parsed and converting to a route overlay using some classes created by Hesham Saeed.
 * @author Joel Dean
 * Requires an instance of the map activity and the application's current context for the progress dialog.
 *
 */

import java.util.ArrayList;
import java.util.Locale;

import android.os.AsyncTask;
import android.os.Environment;


public class Routing extends AsyncTask<PLatLng, Void, Route> {
    protected ArrayList<RoutingListener> _aListeners;
    protected TravelMode _mTravelMode;
    protected boolean _mLocal;
    protected String _mRouteID;

    public enum TravelMode {
        BIKING("biking"),
        DRIVING("driving"),
        WALKING("walking"),
        TRANSIT("transit");

        protected String _sValue;

        private TravelMode(String sValue) {
            this._sValue = sValue;
        }

        protected String getValue() {
            return _sValue;
        }
    }


    public Routing(TravelMode mTravelMode, boolean mLocal, String mRouteID) {
        this._aListeners = new ArrayList<RoutingListener>();
        this._mTravelMode = mTravelMode;
        _mLocal = mLocal;
        _mRouteID = mRouteID;
    }

    public void registerListener(RoutingListener mListener) {
        _aListeners.add(mListener);
    }

    protected void dispatchOnStart() {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingStart();
        }
    }

    protected void dispatchOnFailure() {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingFailure();
        }
    }

    protected void dispatchOnSuccess(Route aRoute) {
        for (RoutingListener mListener : _aListeners) {
            mListener.onRoutingSuccess(aRoute);
        }
    }

    /**
     * Performs the call to the google maps API to acquire routing data and
     * deserializes it to a format the map can display.
     *
     * @param aPoints
     * @return
     */
    @Override
    protected Route doInBackground(PLatLng... aPoints) {

        if (_mLocal) {
            String fileLocation = Environment.getExternalStorageDirectory().getAbsolutePath();
            fileLocation += "/POSEIDON/routes/";
            fileLocation += _mRouteID + "/directions.json";

            return new GoogleParser(fileLocation, true).parse();

        } else {
            for (PLatLng mPoint : aPoints) {
                if (mPoint == null) return null;
            }

            return new GoogleParser(constructURL(aPoints), false).parse();
        }

    }

    protected String constructURL(PLatLng... points) {
        PLatLng start = points[0];
        PLatLng dest = points[1];
        String sJsonURL = "http://maps.googleapis.com/maps/api/directions/json?";

        final StringBuffer mBuf = new StringBuffer(sJsonURL);
        mBuf.append("origin=");
        mBuf.append(start.latitude);
        mBuf.append(',');
        mBuf.append(start.longitude);
        mBuf.append("&destination=");
        mBuf.append(dest.latitude);
        mBuf.append(',');
        mBuf.append(dest.longitude);
        mBuf.append("&sensor=true&mode=");
        mBuf.append(_mTravelMode.getValue());
        mBuf.append("&language=");
        mBuf.append(Locale.getDefault());

        return mBuf.toString();
    }

    @Override
    protected void onPreExecute() {
        dispatchOnStart();
    }

    @Override
    protected void onPostExecute(Route result) {
        if (result == null) {
            dispatchOnFailure();
        } else {
        /*    PolylineOptions mOptions = new PolylineOptions();

            for (LatLng point : result.getPoints()) {
                mOptions.add(point);
            }*/

            dispatchOnSuccess(result);
        }
    }//end onPostExecute method
}
