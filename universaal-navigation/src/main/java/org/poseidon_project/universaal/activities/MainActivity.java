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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.poseidon_project.context.IContextReasoner;
import org.poseidon_project.contexts.ContextReceiver;
import org.poseidon_project.contexts.UIEvent;
import org.poseidon_project.contexts.envir.LocationWeatherContext;
import org.poseidon_project.contexts.envir.weather.BadWeatherContext;
import org.poseidon_project.universaal.POSEIDONUniversaal;
import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.services.ExplicitIntentGenerator;
import org.poseidon_project.universaal.services.INavigationalService;
import org.poseidon_project.universaal.support.RouteImporter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

public class MainActivity extends Activity{

    private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
		checkForFirstTime();

        //Check that the universAAL ontology exists.
        checkForUniversaalOntology();
    }

    private void checkForUniversaalOntology() {
        String filename = "org.poseidon.ontology.jar";
        String extDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + "/data/felix/ontologies";
        copyFile(extDirectory, filename);
        copyFile(extDirectory, "ontologies.cfg");

    }

    private void copyFile(String extDirectory, String filename) {
        File ontology = new File(extDirectory  + "/"  + filename);
        if (! ontology.isFile()) {
            AssetManager assetManager = mContext.getAssets();

            InputStream in = null;
            OutputStream out = null;

            try {
                in =  assetManager.open(filename);
                out = new FileOutputStream(ontology);

                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;

            } catch (Exception e) {
                Log.e("POSEIDON-Universaal", e.getStackTrace().toString());
            }
        }
    }

    private void checkForFirstTime() {
        RouteImporter ri = new RouteImporter(getApplicationContext());
        // (ri.testUnzip(1)) {
        //    Log.d("TestImport 1", "Worked");
        //}
        ///if (ri.testUnzip(2)) {
        //    Log.d("TestImport 2", "Worked");
        //}


	}


	public void onSettingsClick(View v) {

		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);

	}

	public void onVideosClick(View v) {
		LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
	    View popupView = layoutInflater.inflate(R.layout.new_instruction_dialog, null);
	             final PopupWindow popupWindow = new PopupWindow(
	               popupView,
	               LayoutParams.WRAP_CONTENT,
	                     LayoutParams.WRAP_CONTENT);

	             Button btnDismiss = (Button)popupView.findViewById(R.id.done_button);
	             btnDismiss.setOnClickListener(new View.OnClickListener(){

	     @Override
	     public void onClick(View v) {
	      // TODO Auto-generated method stub
	      popupWindow.dismiss();
	     }});

	}

	public void onRoutesClick(View v) {
		Intent intent = new Intent(this, RouteActivity.class);
		startActivity(intent);
	}


}
