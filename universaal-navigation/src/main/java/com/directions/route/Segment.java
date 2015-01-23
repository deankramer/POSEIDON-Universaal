package com.directions.route;

import org.poseidon_project.universaal.R;

import java.util.ArrayList;
import java.util.List;

//by Haseem Saheed


public class Segment implements java.io.Serializable{


	public enum Maneuver {
		turn_sharp_left, uturn_right, turn_slight_right, merge, roundabout_left, roundabout_right
		,uturn_left, turn_slight_left, turn_left, ramp_right, turn_right, fork_right, straight,
		fork_left, ferry_train, turn_sharp_right, ramp_left, ferry, keep_left, keep_right;
	}


	private static final long serialVersionUID = 6728877475338587157L;
	private int id;

	private Maneuver mManeuverType;
    /**
     * Points in this segment. *
     */
    private PLatLng start;
    /**
     * Turn instruction to reach next segment. *
     */
    private String instruction;
    /**
     * Length of segment. *
     */
    private int length;
    /**
     * Distance covered. *
     */
    private double distance;
    /**
     * Segment resources
     */
    private List<String> resources = new ArrayList<String>();
    /**
     * Create an empty segment.
     */

    public Segment(int i) {
    	id=i;
    	mManeuverType = Maneuver.straight;
    }


    /**
     * Set the turn instruction.
     *
     * @param turn Turn instruction string.
     */

    public void setInstruction(final String turn) {
        this.instruction = turn;
    }

    /**
     * Get the turn instruction to reach next segment.
     *
     * @return a String of the turn instruction.
     */

    public String getInstruction() {
        return instruction;
    }

    /**
     * Add a point to this segment.
     *
     * @param point GeoPoint to add.
     */

    public void setPoint(final PLatLng point) {
        start = point;
    }

    /**
     * Get the starting point of this
     * segment.
     *
     * @return a GeoPoint
     */

    public PLatLng startPoint() {
        return start;
    }

    /**
     * Creates a segment which is a copy of this one.
     *
     * @return a Segment that is a copy of this one.
     */

    public Segment copy() {
        final Segment copy = new Segment(id);
        copy.start = start;
        copy.instruction = instruction;
        copy.length = length;
        copy.distance = distance;
        return copy;
    }

    /**
     * @param length the length to set
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public Maneuver getManeuver() {
		return mManeuverType;
	}

	public void setManeuver(Maneuver type) {
		mManeuverType = type;
	}

	public int getManeuverResource() {

		int drawable;

		switch(mManeuverType) {
		case turn_sharp_left	: 	drawable = R.drawable.nav_turn_left;
									break;
		case uturn_right		:	drawable = R.drawable.nav_turn_back;
									break;
		case turn_slight_right	:	drawable = R.drawable.nav_turn_right;
									break;
		case merge 				:	drawable = R.drawable.nav_straight;
									break;
		case roundabout_left	:	drawable = R.drawable.nav_turn_left;
									break;
		case roundabout_right	:	drawable = R.drawable.nav_turn_right;
									break;
		case uturn_left			:	drawable = R.drawable.nav_turn_back;
									break;
		case turn_slight_left	:	drawable = R.drawable.nav_turn_left;
									break;
		case turn_left			:	drawable = R.drawable.nav_turn_left;
									break;
		case ramp_right			:	drawable = R.drawable.nav_turn_right;
									break;
		case turn_right			:	drawable = R.drawable.nav_turn_right;
									break;
		case fork_right			:	drawable = R.drawable.nav_turn_right;
									break;
		case fork_left			:	drawable = R.drawable.nav_turn_left;
									break;
		case ferry_train		:	drawable = R.drawable.nav_straight;
									break;
		case turn_sharp_right 	:	drawable = R.drawable.nav_turn_right;
									break;
		case ramp_left			:	drawable = R.drawable.nav_turn_left;
									break;
		case ferry				:	drawable = R.drawable.nav_straight;
									break;
		case keep_left			:	drawable = R.drawable.nav_turn_left;
									break;
		case keep_right			:	drawable = R.drawable.nav_turn_right;
									break;
		default					:	drawable = R.drawable.nav_straight;
									break;
		}

		return drawable;
	}

}