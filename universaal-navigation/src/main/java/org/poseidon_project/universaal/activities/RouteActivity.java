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
package org.poseidon_project.universaal.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.poseidon_project.universaal.POSEIDONUniversaal;
import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.fragments.ImportRouteDialog;
import org.poseidon_project.universaal.support.IConstants;
import org.poseidon_project.universaal.support.NavigationalDBImpl;
import org.poseidon_project.universaal.support.POSEIDONRoute;
import org.poseidon_project.universaal.support.RouteImporter;
import org.poseidon_project.universaal.support.RouteReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebIconDatabase;
import android.widget.Button;
import android.widget.LinearLayout;

public class RouteActivity extends Activity implements ImportRouteDialog.ImportRouteDialogListener{

	private NavigationalDBImpl mDataBase;
	private Context mContext;
    private static final String LOGTAG = "POSEIDON-Navigation";
    private ImportRouteDialog mImportFrag;
    private BroadcastReceiver mUniversaalReceiver;
    private String mDesktopIPAddress;
    private String mPrefix = RouteActivity.class.getPackage().getName();

    //Globals needed.
    private BroadcastReceiver mContextBR;
    private POSEIDONUniversaal mApplication;

    @Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.routes_activity);
		mContext = getApplicationContext();

		mDataBase = new NavigationalDBImpl(mContext);

		setupRouteChoices();

        setupBroadcastReceivers();

        mApplication = (POSEIDONUniversaal) getApplication();

        //Parameters needed for the weather contexts
        HashMap<String, Object> paras = new HashMap<String, Object>();
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("London,UK");
        locations.add("Southend,UK");
        paras.put("stringPlaces", locations);
        try {
            mApplication.mContextService.addContextRequirementWithParameters(mContext.getPackageName(), "BadWeatherContext", paras);

            mApplication.mContextService.addContextRequirement(mContext.getPackageName(), "GPSIndoorOutdoorContext");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

	}

    private void setupBroadcastReceivers() {
        IntentFilter filter = new IntentFilter("org.poseidon_project.context.CONTEXT_UPDATE");
        mContextBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();

                String contextName = bundle.getString("context_name");
                String contextType = bundle.getString("context_value");

                Log.d(contextName, contextType);

            }
        };

        mContext.registerReceiver(mContextBR, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.routesactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_import:
                importNewRoute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void importNewRoute() {
        mImportFrag = ImportRouteDialog.newInstance();
        mImportFrag.show(getFragmentManager(), "dialog");

        DownloadNewRoutesTask dltask = new DownloadNewRoutesTask();
        dltask.execute();

    }

    public void setupRouteChoices() {
		List<POSEIDONRoute> routes = mDataBase.getAllRoutes();

		LinearLayout viewGroup = (LinearLayout) findViewById(R.id.rootNode);
        if (viewGroup.getChildCount()>0) {
            viewGroup.removeAllViews();
        }

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

    @Override
    public void onFinishDialog(boolean status) {

        if (status) {
                setupRouteChoices();
        }

    }

    public class DownloadNewRoutesTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... args) {

            Context c = getApplicationContext();

            RouteReceiver rr = new RouteReceiver(c);
            RouteImporter ri = new RouteImporter(c);

            String fl = rr.beginListening();
            ri.importRouteArchive(fl);
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(LOGTAG, "Downloaded, importing...");
            updateDialogPercent(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Log.d(LOGTAG, "Downloaded, and imported!");
            mImportFrag.updateProgressBar(100);
            mImportFrag.updateText("Imported");
        }

        public void updateProgressBar(int percent) {
            onProgressUpdate(percent);
        }

    }

    public void updateDialogPercent (int percent) {
        mImportFrag.updateProgressBar(percent);
    }

    public void getDesktopIPAddress() {

        if (mUniversaalReceiver != null) {
            unregisterReceiver(mUniversaalReceiver);
            mUniversaalReceiver = null;
        }

        mUniversaalReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(LOGTAG, "Got Response from universAAL Desktop System");


            }
        };

        String actionNameForReply = mUniversaalReceiver.getClass().getName();

        String category = Intent.CATEGORY_DEFAULT;

        IntentFilter filter = new IntentFilter(actionNameForReply);
        filter.addCategory(category);
        registerReceiver(mUniversaalReceiver, filter);
        
        Intent intent = new Intent(mPrefix + ".GET_IP_ADDRESS");
        intent.putExtra(IConstants.replyToActionArg, actionNameForReply);
        intent.putExtra(IConstants.replyToCategoryArg, category);
        intent.putExtra(IConstants.originDevice, "27.04.19.87");

        sendBroadcast(intent);
        }


    @Override
    public void onStop() {
        super.onStop();
        try {
            mApplication.mContextService.removeContextRequirement(mContext.getPackageName(), "BadWeatherContext");
            mApplication.mContextService.removeContextRequirement(mContext.getPackageName(), "GPSIndoorOutdoorContext");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
