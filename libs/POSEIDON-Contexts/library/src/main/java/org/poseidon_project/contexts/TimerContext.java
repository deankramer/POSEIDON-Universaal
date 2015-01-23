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
package org.poseidon_project.contexts;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

/**
 * Abstract class to hold everything required by timer based context components
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public abstract class TimerContext implements ContextObserver{

	protected String mName;
	protected final Context mContext;
	private Timer mTimer;
	private int mInterval = 2000; //default every 2 seconds
	protected ContextReceiver mReceiver;



	public TimerContext(Context c) {
		mContext = c;
	}

	public TimerContext(Context c, ContextReceiver cr) {
		mContext = c;
		mReceiver = cr;
	}

	public TimerContext(Context c, ContextReceiver cr, int interval, String name) {
		mContext = c;
		mReceiver = cr;
		mInterval = interval;
		mName = name;
	}

	public int getInterval() {
		return mInterval;
	}

	public void setInterval(int interval) {
		mInterval = interval;
	}

	@Override
	public boolean resume() {
		return start();
	}

	@Override
	public boolean pause() {
		return stop();

	}

	@Override
	public boolean start() {
		mTimer = new Timer();

		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				checkContext();
			}
		}, mInterval);

		return true;

	}

	protected abstract void checkContext();

	@Override
	public boolean stop() {
		mTimer.cancel();
		return true;
	}

	@Override
	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	@Override
	public void setContextReceiver(ContextReceiver cr) {
		mReceiver = cr;
	}

}
