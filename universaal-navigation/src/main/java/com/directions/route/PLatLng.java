package com.directions.route;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class PLatLng implements Serializable{

	private static final long serialVersionUID = -2918780198886605725L;
	public double latitude;
	public double longitude;


	public PLatLng(double lati, double longi) {
		latitude = lati;
		longitude = longi;
	}

	public LatLng toLatLng() {
		return new LatLng(latitude, longitude);
	}


}
