package org.poseidon_project.universaal.services;

import org.poseidon_project.universaal.universaal.common.IConstants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EventReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		String lampNumber = intent.getStringExtra(IConstants.lampNumberArg);

		if(Integer.valueOf(lampNumber)==2) {




		}
	}

}
