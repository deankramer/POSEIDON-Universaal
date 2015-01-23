package com.directions.route;
//by Haseem Saheed

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Route implements Serializable{
    /**
	 *
	 */
	private static final long serialVersionUID = -9138739691179229123L;
	private String name;
    private List<PLatLng> points;
    private List<Segment> segments;
    private LinkedList<Segment> segs = new LinkedList<Segment>();
    private String copyright;
    private String warning;
    private String country;
    private int length;
    private String polyline;

    public Route() {
        points = new ArrayList<PLatLng>();
        segments = new ArrayList<Segment>();
    }

    public void addPoint(final PLatLng p) {
        points.add(p);
    }

    public void addPoints(final List<PLatLng> points) {
        this.points.addAll(points);
    }

    public List<PLatLng> getPoints() {
        return points;
    }

    public void addSegment(final Segment s) {
        segments.add(s);
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void addSeg(final Segment s) {
    	segs.add(s);
    }

    public LinkedList<Segment> getSegs() {
    	return segs;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param copyright the copyright to set
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * @return the copyright
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * @param warning the warning to set
     */
    public void setWarning(String warning) {
        this.warning = warning;
    }

    /**
     * @return the warning
     */
    public String getWarning() {
        return warning;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }


    /**
     * @param polyline the polyline to set
     */
    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    /**
     * @return the polyline
     */
    public String getPolyline() {
        return polyline;
    }



}