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
package org.poseidon_project.universaal.contexts.envir;

import org.poseidon_project.contexts.ContextReceiver;
import org.poseidon_project.contexts.LocationContext;

import android.content.Context;
import android.location.Location;

import com.directions.route.PLatLng;
import com.directions.route.Route;
import com.directions.route.Segment;

public class NavigationDeviation extends LocationContext {


	private Route mRoute;
	private Context mContext;
	private Location mCurrentLoc;
	private PLatLng mCurrentTarget;
	private PLatLng mNextTarget;
	private PLatLng mLastTarget;
	private Segment mCurSeg;
	private Segment mNextSeg;
	private float mCurDist = 0;
	private float mNextDist = 0;
	private int mCurDevCounter = 0;
	private int mNextDevCounter = 0;
	private float mDevDist = 0;


	public NavigationDeviation(Context c, ContextReceiver cr) {
		super(c, cr);
	}

	@Override
	protected void checkContext(Location location) {
		onChange(location);
	}

	public void setRoute(Route aRoute) {
		mRoute= aRoute;
		mCurSeg = aRoute.getSegs().poll();
		mCurrentTarget = mCurSeg.startPoint();
		mNextSeg = aRoute.getSegs().poll();
		mNextTarget = mNextSeg.startPoint();
		float[] curDist = new float[1];
		if(mCurrentLoc!=null){
			Location.distanceBetween(mCurrentLoc.getLatitude(), mCurrentLoc.getLongitude(), mCurrentTarget.latitude, mCurrentTarget.longitude, curDist);
			//mContextListener.updateMoveSpeed(String.valueOf(curDist[0]));
		}

	}

	public void onChange(Location cur) {
		if (mRoute!=null) {
			mCurrentLoc = cur;
			float[] curDistf = new float[1];
			float[] nextDistf = new float[1];
			Location.distanceBetween(cur.getLatitude(), cur.getLongitude(), mCurrentTarget.latitude, mCurrentTarget.longitude, curDistf);
			if (mCurDist<curDistf[0]){
				mCurDevCounter++;
				mDevDist+= curDistf[0]-mCurDist;
			} else {
				if(mCurDevCounter>0) {
					mCurDevCounter--;
				}

			}

			if(mCurDevCounter>3){
				Location.distanceBetween(cur.getLatitude(), cur.getLongitude(), mNextTarget.latitude, mNextTarget.longitude, nextDistf);
				if (mNextDist<nextDistf[0]){
					mNextDevCounter++;
				} else {
					mNextDevCounter--;
				}

				if (mNextDevCounter<0) {
					nextTarget();

				} else if (mNextDevCounter>3) {
					//deviated!

					mCurDevCounter=0;
					mNextDevCounter=0;
					mReceiver.newContextValue("deviated", true);

				}
				mNextDist=nextDistf[0];
			}

			mCurDist = curDistf[0];
		}

	}

	private void nextTarget() {
		mCurrentTarget = mNextTarget;
		mCurSeg = mNextSeg;
		mNextSeg = mRoute.getSegs().poll();
		if (mNextSeg!=null) {
			mNextTarget = mNextSeg.startPoint();
			//Toast.makeText(mContext, "You are near your destination", Toast.LENGTH_LONG).show();
		}

		mCurDevCounter=0;
		mNextDevCounter=0;
		mCurDist=mNextDist;
		mNextDist=0;
		mReceiver.newContextValue("next_segment", mCurSeg);
}

}
