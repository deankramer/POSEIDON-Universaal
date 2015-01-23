package com.directions.route;


public interface RoutingListener {
    public void onRoutingFailure();

    public void onRoutingStart();

    public void onRoutingSuccess(Route aRoute);
}