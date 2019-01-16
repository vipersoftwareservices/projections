/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)MapPoint.java	1.00 2003/06/15
 *
 * Copyright 1998-2002 by Viper Software Services
 * 36710 Nichols Ave, Fremont CA, 94536
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 *
 * ---------------------------------------------------------------
 */

package com.viper.projections;

/**
 * A point representing a location on the planet, can be projected 
 * or non-projected values in double precision values.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2003, Original version
 */

public class MapPoint {

	public double lat;

	public double lon;

	/**
	 * Constructs and initializes a point at (0.0, 0.0).
	 *
	 */
	public MapPoint() {
		this.lat = 0.0;
		this.lon = 0.0;
	}

	/**
	 * Constructs and initializes a point at the specified 
	 * (<i>lat</i>,&nbsp;<i>lon</i>) location in the coordinate space. 
	 *
	 * @param       lat   the <i>latitude</i> coordinate
	 * @param       lon   the <i>longitude</i> coordinate
	 */
	public MapPoint(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * Returns the latitude coordinate of the point in double precision.
	 *
	 * @return the latitude coordinate of the point in double precision
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * Returns the longitude coordinate of the point in double precision.
	 *
	 * @return the longitude coordinate of the point in double precision
	 */
	public double getLon() {
		return lon;
	}

	/**
	 *
	 */
	public void set(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * Translates this point, at location (<i>lat</i>,&nbsp;<i>lon</i>), 
	 * by <code>dlat</code> along the <i>latitude</i> axis and <code>dlon</code> 
	 * along the <i>longitudinal</i> axis so that it now represents the point 
	 * (<code>lat</code>&nbsp;<code>+</code>&nbsp;<code>dlat</code>, 
	 * <code>lon</code>&nbsp;<code>+</code>&nbsp;<code>dlon</code>). 
	 *
	 * @param dlat the distance to move this point along the <i>latitude</i> axis
	 * @param dlon the distance to move this point along the <i>longitude</i> axis
	 */

	public void translate(double dlat, double dlon) {
		this.lat += dlat;
		this.lon += dlon;
	}

	/**
	 * Determines whether an instance of <code>MapPoint</code> is equal
	 * to this point.  Two instances of <code>MapPoint</code> are equal if
	 * the values of their <code>lat</code> and <code>lon</code> member 
	 * fields, representing their position in the coordinate space, are
	 * the same.
	 *
	 * @param  obj   an object to be compared with this point
	 *
	 * @return <code>true</code> if the object to be compared is
	 *               an instance of <code>MapPoint</code> and has
	 *               the same values; <code>false</code> otherwise
	 */

	public boolean equals(Object obj) {
		if (obj instanceof MapPoint) {
			MapPoint pt = (MapPoint) obj;
			return (lat == pt.lat) && (lon == pt.lon);
		}
		return super.equals(obj);
	}

	/**
	 * Returns a string representation of this point and its location 
	 * in the (<i>lat</i>,&nbsp;<i>lon</i>) coordinate space. This method is 
	 * intended to be used only for debugging purposes, and the content 
	 * and format of the returned string may vary between implementations. 
	 * The returned string may be empty but may not be <code>null</code>.
	 * 
	 * @return  a string representation of this point
	 */

	public String toString() {
		return "[lat=" + lat + ",lon=" + lon + "]";
	}
}
