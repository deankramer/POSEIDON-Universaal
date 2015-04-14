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

package org.poseidon_project.universaal.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.poseidon_project.universaal.R;

/**
 * Class Description
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 */
public class ImportRouteDialog extends DialogFragment {

    TextView mRemaining;
    ProgressBar mProgessBar;
    Button mDismiss;

    public interface ImportRouteDialogListener {
        void onFinishDialog(boolean status);

    }

    public static ImportRouteDialog newInstance() {

        ImportRouteDialog frag = new ImportRouteDialog();
        return frag;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.importingRoute);

        View view = inflater.inflate(R.layout.fragment_import_route, container);

        mRemaining = (TextView) view.findViewById(R.id.txtRemaining);
        mProgessBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgessBar.setMax(100);
        mDismiss = (Button) view.findViewById(R.id.btnQuit);
        mDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImportRouteDialogListener activity = (ImportRouteDialogListener) getActivity();
                activity.onFinishDialog(true);
                dismiss();
            }
        });

        return view;
    }

    public void updateText(String str) {
        mRemaining.setText(str);
    }

    public void updateProgressBar(int percent) {

        if (percent == 100) {
            mDismiss.setVisibility(View.VISIBLE);
        } else {
            mProgessBar.setProgress(percent);
        }

    }

}
