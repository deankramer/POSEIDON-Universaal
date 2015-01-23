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
package org.poseidon_project.universaal.activities;


import java.util.Map;

import org.poseidon_project.contexts.ContextObserver;
import org.poseidon_project.contexts.ContextReceiver;
import org.poseidon_project.contexts.UIEvent;
import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.contexts.envir.CurrentLocationContext;
import org.poseidon_project.universaal.fragments.NewInstructionDialog;
import org.poseidon_project.universaal.fragments.NewInstructionDialog.NewInstructionDialogListener;
import org.poseidon_project.universaal.fragments.WeatherDialog;
import org.poseidon_project.universaal.services.ExplicitIntentGenerator;
import org.poseidon_project.universaal.services.INavigationalService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.directions.route.PLatLng;
import com.directions.route.Route;
import com.directions.route.Segment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class NavigationActivity extends Activity implements ContextReceiver, NewInstructionDialogListener{

	private static final String LOGTAG = "POSEIDON-Navigation";
	GoogleMap mGMap;
    private INavigationalService mNavService;
    private boolean mBound;
    private BroadcastReceiver mRouteBReceiver;
    private BroadcastReceiver mSegmentBReceiver;
    private BroadcastReceiver mWeatherBReceiver;
    private IntentFilter mRouteFilter = new IntentFilter("org.poseidon_project.universaal.NEW_ROUTE");
    private IntentFilter mSegmentFilter = new IntentFilter("org.poseidon_project.universaal.NEW_SEGMENT");
    private IntentFilter mWeatherFilter = new IntentFilter("org.poseidon_project.universaal.WEATHER");
    private Route mRoute;
    private Segment mCurrentSegment;
    private ContextObserver mCompass;
    private Context mContext;
    private LatLng mCurrentPosition;
    private TextView lblInstructText;
    private ImageView imgInstructPhoto;
    private ContextObserver mCurrentLocationContext;
    private int mRouteid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_activity);
		mContext = getApplicationContext();
		mRouteid = getIntent().getIntExtra("ROUTE_ID", 0);

		 // Get a handle to the Map Fragment
        mGMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        mGMap.setMyLocationEnabled(true);
        setupBroadcastReceivers();
        Intent serviceIntent = new Intent(INavigationalService.class.getName());
        serviceIntent = ExplicitIntentGenerator
                .createExplicitFromImplicitIntent(mContext, serviceIntent);

        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
	}


	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mNavService = INavigationalService.Stub.asInterface(service);
			mBound = true;
			Log.d(LOGTAG, "Nav Service connected");
			 try {
			//	 Thread.sleep(800);
					mNavService.startNavigation(mRouteid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
			mNavService = null;
			Log.d(LOGTAG, "Nav Service disconnected");
		}
	};

	public void onInit() {

	}


	public void setupBroadcastReceivers() {
		mRouteBReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();

				mRoute = (Route) bundle.get("ROUTE");
				startLocation();
				drawRoute((String)bundle.get("ORIGIN"));
			}
		};

		registerReceiver(mRouteBReceiver, mRouteFilter);

		mSegmentBReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();

				mCurrentSegment = mRoute.getSegments().get(bundle.getInt("SEGMENT_ID") - 1);
				updateRoute();

			}

		};

		registerReceiver(mSegmentBReceiver, mSegmentFilter);

		mWeatherBReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Bundle bundle = intent.getExtras();

				WeatherDialog frag = WeatherDialog.newInstance(bundle.getString("TEXT", "nothing"),
                        "Step " + String.valueOf(mCurrentSegment.getId()),
                        bundle.getInt("DRAWABLE"));
				frag.show(getFragmentManager(), "dialog");

			}

		};

		registerReceiver(mWeatherBReceiver, mWeatherFilter);
	}

	protected void startLocation() {
		mCurrentLocationContext = new CurrentLocationContext(mContext, this);
		mCurrentLocationContext.start();
	}


	protected void updateRoute() {
		showNewInstructionDialog();

		lblInstructText.setText(mCurrentSegment.getInstruction());
		imgInstructPhoto.setImageResource(mCurrentSegment.getManeuverResource());

	}


	private void showNewInstructionDialog() {
		NewInstructionDialog frag = NewInstructionDialog.newInstance(mCurrentSegment, "Step " + String.valueOf(mCurrentSegment.getId()));
		frag.show(getFragmentManager(), "dialog");
	}


	protected void drawRoute(String origin) {
		PolylineOptions mPolyOptions = new PolylineOptions();

		PLatLng start = mRoute.getPoints().get(0);
		mCurrentSegment = mRoute.getSegments().get(0);
		mCurrentPosition = start.toLatLng();
	     for (PLatLng point : mRoute.getPoints()) {
               mPolyOptions.add(point.toLatLng());
           }
		  PolylineOptions polyoptions = new PolylineOptions();
	      polyoptions.color(Color.BLUE);
	      polyoptions.width(10);
	      polyoptions.addAll(mPolyOptions.getPoints());
	      mGMap.addPolyline(polyoptions);
	      mGMap.addMarker(new MarkerOptions().position(start.toLatLng()).title("me"));
		  mGMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start.toLatLng(), 15));
	      mGMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

	      lblInstructText = (TextView) findViewById(R.id.lblInstructText);
          lblInstructText.setText(lblInstructText.getText() + origin);
	      imgInstructPhoto = (ImageView) findViewById(R.id.imgInstructPhoto);
          imgInstructPhoto.setImageResource(R.drawable.img_home);

	     // mCompass = new CompassContext(mContext, this);
	     // mCompass.start();
	}


	@Override
	public void newContextValue(String name, long value) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target(mCurrentPosition)
	    .bearing(value)
	    .zoom(16)
	    .build();
		Log.d(LOGTAG, String.valueOf(value));

		mGMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}


	@Override
	public void newContextValue(String name, double value) {
		// TODO Auto-generated method stub

	}


	@Override
	public void newContextValue(String name, boolean value) {
		// TODO Auto-generated method stub

	}


	@Override
	public void newContextValue(String name, String value) {
		// TODO Auto-generated method stub

	}


	@Override
	public void newContextValue(String name, Object value) {
		if (name.equals("location")) {

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


	@Override
	public void onFinishDialog(boolean status) {


	}




}
