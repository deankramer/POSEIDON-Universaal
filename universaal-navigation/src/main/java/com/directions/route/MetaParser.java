/*
 * Copyright 2014 POSEIDON Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * /
 */

package com.directions.route;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.poseidon_project.universaal.support.POSEIDONRoute;

/**
 * Parses the meta.json Navigation File.
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class MetaParser extends XMLParser{

    public MetaParser(String fileLocation, boolean local) {
        super(fileLocation, local);
    }

    public POSEIDONRoute parse() {
        final String result = convertStreamToString(this.getInputStream());
        if (result == null) return null;

        final POSEIDONRoute route = new POSEIDONRoute();

        try {

            final JSONObject json = new JSONObject(result);

            route.setRouteId(json.getInt("id"));
            route.setTitle(json.getString("title"));
            route.setStart_location(json.getString("start_location"));
            route.setEnd_location(json.getString("end_location"));
            route.setStart_longitude(json.getDouble("start_longitude"));
            route.setStart_latitude(json.getDouble("start_latitude"));
            route.setEnd_longitude(json.getDouble("end_longitude"));
            route.setEnd_latitude(json.getDouble("end_latitude"));
            route.setResource(json.getString("resource"));

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return route;
    }
}
