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
package org.poseidon_project.universaal.fragments;

import org.poseidon_project.universaal.R;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherDialog extends DialogFragment{


	private String mText;
	private static int mPicture;

	public void setText(String text) {
		mText = text;
	}

	public static WeatherDialog newInstance(String text, String title, int picture) {
		WeatherDialog frag = new WeatherDialog();
		frag.setText(text);
		Bundle args = new Bundle();
		args.putString("title", title);
		mPicture = picture;
		frag.setArguments(args);
		return frag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.new_instruction_dialog, container);
		ImageView img = (ImageView) view.findViewById(R.id.imgDirection);
		TextView txt = (TextView) view.findViewById(R.id.lblDirection);
		Button btn = (Button) view.findViewById(R.id.done_button);

		img.setImageResource(mPicture);
		txt.setText(mText);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NewInstructionDialog.NewInstructionDialogListener activity = (NewInstructionDialog.NewInstructionDialogListener) getActivity();
				activity.onFinishDialog(true);
				dismiss();

			}

		});

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return view;
	}


}
