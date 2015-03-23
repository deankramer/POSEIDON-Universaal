/*
 * Copyright 2015 POSEIDON Project
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

package org.poseidon_project.universaal.breceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import org.poseidon_project.universaal.R;
import org.poseidon_project.universaal.activities.NavigationActivity;

/**
 * Class Description
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class CalendarBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("org.poseidon_project.CALENDAR_FINAL_REMINDER")) {

            Bundle bundle = intent.getExtras();

            String reminderTitle = bundle.getString("RTITLE");
            String reminderText = bundle.getString("RTEXT");
            int routeID = bundle.getInt("ROUTE_ID");

            showNotification(context, reminderTitle, reminderText, routeID);



        }


    }

    public void showNotification(Context context, String reminderTitle,
                                 String reminderText, int routeID) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        long[] vibrate = {0,100,200,300,200,1000,500,500,1000,200};
        int resource = R.drawable.ic_launcher;
        Notification.Builder mBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(resource)
                        .setContentTitle(reminderTitle)
                        .setContentText(reminderText)
                        .setVibrate(vibrate)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setLights(Color.BLUE, 500, 500);

        Intent resultIntent = new Intent(context, NavigationActivity.class);
        resultIntent.putExtra("ROUTE_ID", routeID);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(1, mBuilder.build());
    }
}
