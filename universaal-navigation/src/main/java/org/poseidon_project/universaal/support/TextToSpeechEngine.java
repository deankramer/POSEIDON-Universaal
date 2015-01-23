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
package org.poseidon_project.universaal.support;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * Main Class for handling text-to-speech
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public class TextToSpeechEngine {

	private TextToSpeech mSpeechEngine;
	private Context mContext;
	private boolean mInit = false;

	public TextToSpeechEngine(Context c) {
		mContext = c;
		setupEngine(Locale.getDefault());
	}

	public TextToSpeechEngine(Context c, Locale loc) {
		mContext =  c;
		setupEngine(loc);
	}

	private void setupEngine(final Locale loc) {


                mSpeechEngine = new TextToSpeech(mContext,
                        new TextToSpeech.OnInitListener() {
                            @Override
                            public void onInit(int status) {
                                if(status != TextToSpeech.ERROR){
                                    mSpeechEngine.setLanguage(loc);
                                    mInit = true;
                                }
                            }
                        });

	}

	public void changeLocale(Locale loc) {
		mSpeechEngine.setLanguage(loc);
	}

	public Locale getLanguage() {
		return mSpeechEngine.getLanguage();
	}

	public void stop() {
		mSpeechEngine.stop();
		mSpeechEngine.shutdown();
	}

	public void speak(final String toSpeak) {

		/*final Runnable runnable = new Runnable() {

			@Override
	    	public void run() {
                while (!mInit) {
                    try {
                        Log.e("tts", "waiting");
                        wait(500);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                }

          */
        //while (!mInit) {
        //    try {
        //        Log.e("tts", "waiting");
        //        wait(500);
        //    } catch (InterruptedException e) {

        //        e.printStackTrace();
        //    }
       // }
        mSpeechEngine.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
           // }};

        //runnable.run();
	}

}
