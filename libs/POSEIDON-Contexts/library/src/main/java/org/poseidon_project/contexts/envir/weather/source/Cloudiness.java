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
package org.poseidon_project.contexts.envir.weather.source;

/**
 * This class holds information about cloudiness
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public class Cloudiness {

	public enum Unit {
		PERCENT, OKTA;
	}

	private Unit mCloudUnit;
	private int mCloudValue = Integer.MIN_VALUE;
	private String mCloudDesc;

	public Cloudiness (Unit cloudUnit) {
		mCloudUnit = cloudUnit;
	}

	public Cloudiness (Unit cloudUnit, int value) {
		mCloudUnit = cloudUnit;
		mCloudValue = value;
	}

	public void setValue(int value) {
		mCloudValue = value;
	}

	public void setValue(int value, Unit cloudUnit) {
		if (value == Integer.MIN_VALUE) {
			mCloudValue = value;
			return;
		}

		mCloudValue = convertCloudiness(value, cloudUnit, mCloudUnit);
	}

	public int getValue() {
		return mCloudValue;
	}

	public Unit getCloudinessUnit() {
		return mCloudUnit;
	}

	public static int convertCloudiness(int value, Unit currentCloudUnit, Unit desiredCloudUnit) {

		switch (currentCloudUnit) {
		case PERCENT:
			switch (desiredCloudUnit) {
			case PERCENT:
				return value;
			case OKTA:
				return (int)Math.round(8.0 / 100 * value);
			}

		case OKTA:
			switch (desiredCloudUnit) {
			case PERCENT:
				return (int)Math.round(100 / 8.0 * value);
			case OKTA:
				return value;
			}
		}

		return value;
	}

	public String getDescription() {
		return mCloudDesc;
	}

	public void setDescription(String desc) {
		mCloudDesc = desc;
	}

}
