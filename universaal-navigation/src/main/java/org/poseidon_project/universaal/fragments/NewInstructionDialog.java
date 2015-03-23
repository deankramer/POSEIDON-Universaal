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

import com.directions.route.Segment;

public class NewInstructionDialog extends DialogFragment{


	private Segment mCurrentSegment;


	public interface NewInstructionDialogListener {
		void onFinishDialog(boolean status);
	}

	public void setSegment(Segment seg) {
		mCurrentSegment = seg;
	}

	public static NewInstructionDialog newInstance(Segment seg, String title) {
		NewInstructionDialog frag = new NewInstructionDialog();
		frag.setSegment(seg);
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.new_instruction_dialog, container);
		ImageView img = (ImageView) view.findViewById(R.id.imgDirection);
		TextView txt = (TextView) view.findViewById(R.id.lblDirection);
		Button btn = (Button) view.findViewById(R.id.done_button);

		img.setImageResource(mCurrentSegment.getManeuverResource());
		txt.setText(mCurrentSegment.getInstruction());
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NewInstructionDialogListener activity = (NewInstructionDialogListener) getActivity();
				activity.onFinishDialog(true);
				dismiss();

			}

		});

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return view;
	}


}
