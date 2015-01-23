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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.support.NavigationalDBImpl;
import org.poseidon_project.universaal.support.POSEIDONRoute;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class RouteActivity extends Activity{

	private NavigationalDBImpl mDataBase;
	private Context mContext;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.routes_activity);
		mContext = getApplicationContext();

		mDataBase = new NavigationalDBImpl(mContext);

		setupRouteChoices();

	}


	public void setupRouteChoices() {
		List<POSEIDONRoute> routes = mDataBase.getAllRoutes();

		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.rootNode);

		for (final POSEIDONRoute route : routes) {
			Button routeButton = new Button(mContext);
            String location = Environment.getExternalStorageDirectory().getAbsolutePath();
            location += "/POSEIDON/routes/";
            location += route.getRouteId() + "/media/" + route.getResource();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(new File(location));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Drawable routePhoto = Drawable.createFromStream(fis, null);



			routeButton.setText(route.getTitle());
			routeButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										ViewGroup.LayoutParams.WRAP_CONTENT));
            routeButton.setCompoundDrawablesWithIntrinsicBounds(routePhoto, null, null, null);
			//routeButton.setCompoundDrawablesRelative(mContext.getResources().getDrawable(R.drawable.ic_launcher), null, null, null);
			routeButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RouteActivity.this, NavigationActivity.class);
					intent.putExtra("ROUTE_ID", route.getRouteId());
					startActivity(intent);
				}
			});

			viewGroup.addView(routeButton);

		}

	}

}
