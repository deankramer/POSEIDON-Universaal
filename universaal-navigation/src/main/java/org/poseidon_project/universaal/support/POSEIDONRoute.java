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

import java.io.Serializable;

/**
 * A basic data structure for use with the db
 *
 * @author Dean Kramer <d.kramer@mdx.ac.uk>
 *
 */
public class POSEIDONRoute implements Serializable{

	private static final long serialVersionUID = -2472801907949389479L;
	private int mRouteId;
	private String mTitle;
	private double mStart_longitude;
	private double mStart_latitude;
	private double mEnd_longitude;
	private double mEnd_latitude;
	private String mResource;


	public POSEIDONRoute() {

	}




	public int getRouteId() {
		return mRouteId;
	}




	public void setRouteId(int routeId) {
		mRouteId = routeId;
	}




	public String getTitle() {
		return mTitle;
	}




	public void setTitle(String routePlace) {
		mTitle = routePlace;
	}




	public double getStart_longitude() {
		return mStart_longitude;
	}




	public void setStart_longitude(double start_longitude) {
		mStart_longitude = start_longitude;
	}




	public double getStart_latitude() {
		return mStart_latitude;
	}




	public void setStart_latitude(double start_latitude) {
		mStart_latitude = start_latitude;
	}




	public double getEnd_longitude() {
		return mEnd_longitude;
	}




	public void setEnd_longitude(double end_longitude) {
		mEnd_longitude = end_longitude;
	}




	public double getEnd_latitude() {
		return mEnd_latitude;
	}




	public void setEnd_latitude(double end_latitude) {
		mEnd_latitude = end_latitude;
	}

    public String getResource() { return mResource;}

    public void setResource(String res) { mResource = res;}



}
