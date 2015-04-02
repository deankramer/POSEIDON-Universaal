/*Copyright 2015 POSEIDON Project

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
package org.poseidon_project.universaal.support;

import java.util.List;
import java.util.Vector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NavigationalDBImpl {

	private DbHelper mDbHelper;
	private static final String ROUTE_TABLE = "saved_routes";
	private static final String LOG_TAG = "POSEIDON-DB";

	public NavigationalDBImpl (Context c) {

		mDbHelper = new DbHelper(c);

	}

	public void closeDB() {
		mDbHelper.close();
	}

	public List<POSEIDONRoute> getAllRoutes() {

		List<POSEIDONRoute> routes = new Vector<POSEIDONRoute>();

		try {
			SQLiteDatabase sqlite = mDbHelper.getReadableDatabase();
			Cursor crsr = sqlite.rawQuery(
					"Select _id, title, start_location, end_location, start_longitude, start_latitude, end_longitude" +
                            ", end_latitude, resource from saved_routes;",
					null);
			crsr.moveToFirst();

			int numRows = crsr.getCount();
			for (int i = 0; numRows > i; i++) {
				POSEIDONRoute route = new POSEIDONRoute();
				route.setRouteId(crsr.getInt(0));
				route.setTitle(crsr.getString(1));
                route.setStart_location(crsr.getString(2));
                route.setEnd_location(crsr.getString(3));
				route.setStart_longitude(Double.parseDouble(crsr.getString(4)));
				route.setStart_latitude(Double.parseDouble(crsr.getString(5)));
				route.setEnd_longitude(Double.parseDouble(crsr.getString(6)));
				route.setEnd_latitude(Double.parseDouble(crsr.getString(7)));
				route.setResource(crsr.getString(8));
				routes.add(route);
				crsr.moveToNext();
			}

		} catch (Exception sqlerror) {
			Log.e(LOG_TAG, sqlerror.getMessage());
		}

		return routes;
	}

	public POSEIDONRoute getRoute(int routeId) {

		try {
			SQLiteDatabase sqlite = mDbHelper.getReadableDatabase();
			Cursor crsr = sqlite.rawQuery(
					"Select _id, title, start_location, end_location, start_longitude, start_latitude, end_longitude" +
                            ", end_latitude, resource from saved_routes where _id="
					+ routeId + ";",
					null);
			crsr.moveToFirst();

			int numRows = crsr.getCount();
			if (numRows == 1) {
				POSEIDONRoute route = new POSEIDONRoute();
                route.setRouteId(crsr.getInt(0));
                route.setTitle(crsr.getString(1));
                route.setStart_location(crsr.getString(2));
                route.setEnd_location(crsr.getString(3));
                route.setStart_longitude(Double.parseDouble(crsr.getString(4)));
                route.setStart_latitude(Double.parseDouble(crsr.getString(5)));
                route.setEnd_longitude(Double.parseDouble(crsr.getString(6)));
                route.setEnd_latitude(Double.parseDouble(crsr.getString(7)));
                route.setResource(crsr.getString(8));
				crsr.moveToNext();
				return route;
			}

		} catch (Exception sqlerror) {
			Log.e(LOG_TAG, sqlerror.getMessage());
		}

		return null;
	}


	public boolean insertRoute(POSEIDONRoute route) {

		try {
			SQLiteDatabase sqlite = mDbHelper.getWritableDatabase();

			ContentValues initialValues = new ContentValues();
            initialValues.put("_id", route.getRouteId());
			initialValues.put("title", route.getTitle());
            initialValues.put("start_location", route.getStart_location());
            initialValues.put("end_location", route.getEnd_location());
			initialValues.put("start_longitude", route.getStart_longitude());
			initialValues.put("start_latitude", route.getStart_latitude());
			initialValues.put("end_longitude", route.getEnd_longitude());
			initialValues.put("end_latitude", route.getEnd_latitude());
			initialValues.put("resource", route.getResource());
			sqlite.insert(ROUTE_TABLE, null, initialValues);
			sqlite.close();
			return true;

		} catch (Exception sqlerror) {
			Log.v(LOG_TAG, sqlerror.getMessage());
			return false;
		}
	}


}
