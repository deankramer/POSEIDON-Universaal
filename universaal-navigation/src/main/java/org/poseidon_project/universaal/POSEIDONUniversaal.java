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

package org.poseidon_project.universaal;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import org.poseidon_project.context.IContextReasoner;
import org.poseidon_project.universaal.services.ExplicitIntentGenerator;
import org.poseidon_project.universaal.services.INavigationalService;

/**
 * Global Application Class
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class POSEIDONUniversaal extends Application{

    public IContextReasoner mContextService;
    private boolean mBound = false;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        Intent serviceIntent = new Intent(IContextReasoner.class.getName());
        serviceIntent = ExplicitIntentGenerator
                .createExplicitFromImplicitIntent(mContext, serviceIntent);

        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);

    }


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mContextService  = IContextReasoner.Stub.asInterface(iBinder);
            mBound = ! mBound;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mContextService = null;
            mBound = ! mBound;
        }
    };
}
