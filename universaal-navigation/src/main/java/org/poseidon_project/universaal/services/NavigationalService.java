/*Copyright 2014 POSEIDON Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.poseidon_project.universaal.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.poseidon_project.contexts.ContextObserver;
import org.poseidon_project.contexts.ContextReceiver;
import org.poseidon_project.contexts.UIEvent;
import org.poseidon_project.contexts.envir.LocationWeatherContext;
import org.poseidon_project.contexts.envir.weather.BadWeatherContext;
import org.poseidon_project.contexts.hardware.GPSIndoorOutdoorContext;
import org.poseidon_project.contexts.hardware.LightContext;
import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.activities.NavigationActivity;
import org.poseidon_project.universaal.support.NavigationalDBImpl;
import org.poseidon_project.universaal.support.POSEIDONRoute;
import org.poseidon_project.universaal.support.TextToSpeechEngine;
import org.poseidon_project.universaal.universaal.common.IConstants;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.directions.route.PLatLng;
import com.directions.route.Route;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.directions.route.Segment;

public class NavigationalService extends Service implements RoutingListener, ContextReceiver{

	private static final String LOGTAG = "POSEIDON-NAVService";
	private static final String NEW_ROUTE_INTENT = "org.poseidon_project.universaal.NEW_ROUTE";
	private static final String NEW_SEGMENT_INTENT = "org.poseidon_project.universaal.NEW_SEGMENT";
	private static final String WEATHER_INTENT = "org.poseidon_project.universaal.WEATHER";

	private final IBinder mLocalBinder = new LocalBinder();

	protected PLatLng mStartPosition;
    protected PLatLng mEndPosition;
    private TextToSpeechEngine mTtse;
    private Route mRoute;
    private Segment mCurrentSegment;
    private Context mContext;
    private ContextObserver mLight;
    private NavigationalDBImpl mDataBase;
    private GPSIndoorOutdoorContext mOutdoor;
    private BadWeatherContext weather;
    private boolean mWarmKitNeeded = false;
    private boolean mRainKitNeeded = false;
    private boolean startedRoute = false;

    public class LocalBinder extends Binder {
		public NavigationalService getService() {
			return NavigationalService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
        Log.e(LOGTAG, "Started Service");
		mContext = getApplicationContext();
		mTtse = new TextToSpeechEngine(mContext);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mDataBase = new NavigationalDBImpl(mContext);
		mLight = new LightContext(mContext, this);
		mOutdoor = new GPSIndoorOutdoorContext(mContext, this);
		//weather = new LocationWeatherContext(getApplicationContext(), this);
        weather = new BadWeatherContext(mContext, this);



	}




	@Override
	public IBinder onBind(Intent intent) {
		if (INavigationalService.class.getName().equals(intent.getAction())) {
			return mNavService;
		}
		return mLocalBinder;
	}

	public final INavigationalService.Stub mNavService = new INavigationalService.Stub() {

		@Override
		public void startNavigation(int routeId)
				throws RemoteException {

			POSEIDONRoute route = mDataBase.getRoute(routeId);

            List<Location> places = new ArrayList<Location>();
            Location origin = new Location("nav_service");
            origin.setLatitude(route.getStart_latitude());
            origin.setLongitude(route.getStart_longitude());

            weather.setPlace(places, false);
            weather.start();
				//mStartPosition = new PLatLng(route.getStart_latitude(),route.getStart_longitude());
		       // mEndPosition = new PLatLng(route.getEnd_latitude(), route.getEnd_longitude());

                //mStartPosition = new PLatLng(51.588426,-0.2239829);
                //mEndPosition = new PLatLng(51.588939, -0.228170);
		        Routing routing = new Routing(Routing.TravelMode.WALKING, true, String.valueOf(routeId));
		        routing.registerListener(NavigationalService.this);
		        routing.execute(mStartPosition, mEndPosition);
		        mOutdoor.start();




		}

		@Override
		public void getCurrentRoute() throws RemoteException {
			// TODO Auto-generated method stub

		}




	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(LOGTAG, "Called onDestroy, service shutting down");
	}

	@Override
	public void onRoutingFailure() {


	}

	@Override
	public void onRoutingStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoutingSuccess(Route aRoute) {
        mRoute = aRoute;

        Intent intent = new Intent();
        intent.setAction(NEW_ROUTE_INTENT);
        intent.putExtra("ROUTE", aRoute);
        intent.putExtra("ORIGIN", "home");
        mContext.sendBroadcast(intent);

	/*	mRoute = aRoute;
		mCurrentSegment = aRoute.getSegments().get(0);
		newNotification("POSEIDON Navigation", mCurrentSegment.getInstruction(), mCurrentSegment.getManeuverResource());
		Intent intent = new Intent();
		intent.setAction(NEW_ROUTE_INTENT);
		intent.putExtra("ROUTE", aRoute);
		mContext.sendBroadcast(intent);
		mTtse.speak(mCurrentSegment.getInstruction());

		Intent intent1 = new Intent();
		intent1.setAction(NEW_SEGMENT_INTENT);
		intent1.putExtra("SEGMENT_ID", mCurrentSegment.getId());
		mContext.sendBroadcast(intent1);
		//weather.setPlace("London,UK");
		//weather.start();
		mLight.start();
		*/
        int weatherMessage;
        int drawable;
        if ((mRainKitNeeded) && (! mWarmKitNeeded)) {
            weatherMessage = R.string.rainForecast;
            drawable = R.drawable.img_rain;
        } else if ((! mRainKitNeeded) && (mWarmKitNeeded)) {
            weatherMessage = R.string.coldForecast;
            drawable = R.drawable.img_cold;
        } else {
            weatherMessage = R.string.coldAndRainForcast;
            drawable = R.drawable.img_cold_rain;
        }

        String weather = getString(weatherMessage);
        mTtse.speak(weather);
        Intent intent1 = new Intent();
        intent1.setAction(WEATHER_INTENT);
        intent1.putExtra("TEXT", weather);
        intent1.putExtra("DRAWABLE", drawable);
        mContext.sendBroadcast(intent1);
	}

	private void newNotification(String title, String text, int resource) {

		if (resource==0) {
			resource = R.drawable.ic_launcher;
		}
	    Notification.Builder mBuilder =
	            new Notification.Builder(this)
	                    .setSmallIcon(resource)
	                    .setContentTitle(title)
	                    .setContentText(text);

	    Intent resultIntent = new Intent(this, NavigationActivity.class);

	    PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    mBuilder.setContentIntent(resultPendingIntent);

	    startForeground(1000,mBuilder.build());  // 1000 - is Id for the notification
	}

	private void updateNotification(String title, String text, int resource) {
		if (resource==0) {
			resource = R.drawable.ic_launcher;
		}
		NotificationManager notifyMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		Notification.Builder mBuilder =
	            new Notification.Builder(this)
	                    .setSmallIcon(resource)
	                    .setContentTitle(title)
	                    .setContentText(text);

		notifyMan.notify(1000, mBuilder.build());
	}

	private void nextSegment(Segment seg) {
		mCurrentSegment = seg;
		updateNotification("POSEIDON Navigation", mCurrentSegment.getInstruction(), mCurrentSegment.getManeuverResource());
		Intent intent = new Intent();
		intent.setAction(NEW_SEGMENT_INTENT);
		intent.putExtra("SEGMENT_ID", seg.getId());
		mContext.sendBroadcast(intent);
		mTtse.speak(mCurrentSegment.getInstruction());

	}

    private void startRoute () {
        mCurrentSegment = mRoute.getSegments().get(0);
        Intent intent1 = new Intent();
        intent1.setAction(NEW_SEGMENT_INTENT);
        intent1.putExtra("SEGMENT_ID", mCurrentSegment.getId());
        mContext.sendBroadcast(intent1);
        mTtse.speak(mCurrentSegment.getInstruction());
        newNotification("POSEIDON Navigation", mCurrentSegment.getInstruction(), mCurrentSegment.getManeuverResource());
    }

	private void deviation() {
		mTtse.speak("Please Return to the current route");
		updateNotification("POSEIDON Navigation", "Please return to the current route", R.drawable.nav_turn_back);
	}

	@Override
	public void newContextValue(String name, long value) {

		if (name.equals("sensor.light_lumens")) {
			Intent intent = new Intent(IConstants.lampStateChangedAction);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			int lampnum = 1;
			intent.putExtra(IConstants.lampNumberArg, String.valueOf(lampnum));
			intent.putExtra(IConstants.brightnessArg, (int)value);
			mContext.sendBroadcast(intent);
		}

	}




	@Override
	public void newContextValue(String name, double value) {
		// TODO Auto-generated method stub

	}




	@Override
	public void newContextValue(String name, boolean value) {
		if (name.equals("deviated")) {

		}

		if (name.equals("sensor.gps_indoor_outdoor")) {
			if (value) {
              /*  String rain = "It appears to be raining today, don't forget to bring an umbrella";
                String cold = "it is cold today, please wear a coat";
				String weather = "";

                if ((mRainKitNeeded) && (! mWarmKitNeeded)) {
                    weather = rain;
                } else if ((! mRainKitNeeded) && (mWarmKitNeeded)) {
                    weather = cold;
                } else {
                    weather = rain + ", and " + cold;
                }

				mTtse.speak(weather);
				Intent intent1 = new Intent();
				intent1.setAction(WEATHER_INTENT);
				intent1.putExtra("TEXT", weather);
				mContext.sendBroadcast(intent1);
				*/
                if (! startedRoute) {
                    startRoute();
                    startedRoute = ! startedRoute;
                }


			}
		}

        if (name.equals("weather:cold")) {
            mWarmKitNeeded = true;
        }

        if (name.equals("weather:rain")) {
            mRainKitNeeded = true;
        }

        if (name.equals("weather:rainAndCold")) {
            mRainKitNeeded = true;
            mWarmKitNeeded = true;
        }

	}




	@Override
	public void newContextValue(String name, String value) {
		// TODO Auto-generated method stub

	}




	@Override
	public void newContextValue(String name, Object value) {
		if (name.equals("next_segment")) {
			nextSegment((Segment) value);
		}
	}




	@Override
	public void newContextValues(Map<String, String> values) {
		// TODO Auto-generated method stub

	}




	@Override
	public void newUIEvent(UIEvent event) {
		// TODO Auto-generated method stub

	}





}
