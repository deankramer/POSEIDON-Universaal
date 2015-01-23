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

public class CurrentLocationContext extends LocationContext {

	public CurrentLocationContext(Context c, ContextReceiver cr) {
		super(c, cr);
	}

	@Override
	protected void checkContext(Location location) {

		mReceiver.newContextValue("location", location);

	}

}
