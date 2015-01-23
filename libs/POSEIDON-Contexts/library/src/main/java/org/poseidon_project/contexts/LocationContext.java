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
package org.poseidon_project.contexts;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Abstract class for holding everything needed for location based context components
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public abstract class LocationContext implements ContextObserver, LocationListener {

	private String mName;
	protected LocationManager mLocationManager;
	private int mMinTime = 3000;
	private int mMinDistance = 10;
	private final Context mContext;
	protected ContextReceiver mReceiver;
	private String mProvider = LocationManager.GPS_PROVIDER;
	private String mIdealProvider = LocationManager.GPS_PROVIDER;


	public LocationContext (Context c) {
		mContext = c;
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	public LocationContext (Context c, ContextReceiver cr) {
		mContext = c;
		mReceiver = cr;
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	public LocationContext (Context c, ContextReceiver cr, int minTime, int minDistance, String name) {
		mContext = c;
		mReceiver = cr;
		mMinTime = minTime;
		mMinDistance = minDistance;
		mName = name;
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	public LocationContext (Context c, ContextReceiver cr, int minTime, int minDistance, String provider, String name) {
		mContext = c;
		mReceiver = cr;
		mMinTime = minTime;
		mMinDistance = minDistance;
		mProvider = provider;
		mIdealProvider = provider;
		mName = name;
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public boolean start() {
		mLocationManager.requestLocationUpdates(mProvider, mMinTime, mMinDistance, this);
		return true;
	}


	@Override
	public boolean pause() {
		return stop();
	}


	@Override
	public boolean resume() {
		return start();
	}

	@Override
	public boolean stop() {
		mLocationManager.removeUpdates(this);
		return true;
	}

	@Override
	public void setContextReceiver(ContextReceiver cr) {
		mReceiver = cr;
	}


	@Override
	public void onLocationChanged(Location location) {
		checkContext(location);

	}


	protected abstract void checkContext(Location location);

	@Override
	public void onProviderDisabled(String provider) {
		if (provider.equals(mIdealProvider)) {
			mProvider = LocationManager.GPS_PROVIDER;

			if (! mLocationManager.isProviderEnabled(mProvider)) {
				Intent gpsOptionIntent = new Intent (android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(gpsOptionIntent);
			}
		}

	}


	@Override
	public void onProviderEnabled(String provider) {
		if ((provider.equals(mIdealProvider)) && (! provider.equals(mProvider))) {
			mLocationManager.removeUpdates(this);
			mProvider = provider;
			mLocationManager.requestLocationUpdates(mProvider, mMinTime, mMinDistance, this);
		}
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	public int getMinTime() {
		return mMinTime;
	}

	public void setMinTime(int mMinTime) {
		this.mMinTime = mMinTime;
	}

	public int getMinDistance() {
		return mMinDistance;
	}

	public void setMinDistance(int mMinDistance) {
		this.mMinDistance = mMinDistance;
	}

	@Override
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

}
