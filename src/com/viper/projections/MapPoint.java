/*
 * -----------------------------------------------------------------------------
 *                      VIPER SOFTWARE SERVICES
 * -----------------------------------------------------------------------------
 *
 * MIT License
 * 
 * Copyright (c) #{classname}.html #{util.YYYY()} Viper Software Services
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 *
 * -----------------------------------------------------------------------------
 */


package com.viper.projections;
 
/**
 * A point representing a location on the planet, can be projected or non-projected values in double
 * precision values.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2003, Original version
 */

public class MapPoint implements Comparable {

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
     * Constructs and initializes a point at the specified (<i>lat</i>,&nbsp;<i>lon</i>) location in
     * the coordinate space.
     *
     * @param lat
     *            the <i>latitude</i> coordinate
     * @param lon
     *            the <i>longitude</i> coordinate
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
     * Translates this point, at location (<i>lat</i>,&nbsp;<i>lon</i>), by <code>dlat</code> along
     * the <i>latitude</i> axis and <code>dlon</code> along the <i>longitudinal</i> axis so that it
     * now represents the point (<code>lat</code>&nbsp;<code>+</code>&nbsp;<code>dlat</code>,
     * <code>lon</code>&nbsp;<code>+</code>&nbsp;<code>dlon</code>).
     *
     * @param dlat
     *            the distance to move this point along the <i>latitude</i> axis
     * @param dlon
     *            the distance to move this point along the <i>longitude</i> axis
     */

    public void translate(double dlat, double dlon) {
        this.lat += dlat;
        this.lon += dlon;
    }

    /**
     * Determines whether an instance of <code>MapPoint</code> is equal to this point. Two instances
     * of <code>MapPoint</code> are equal if the values of their <code>lat</code> and
     * <code>lon</code> member fields, representing their position in the coordinate space, are the
     * same.
     *
     * @param obj
     *            an object to be compared with this point
     *
     * @return <code>true</code> if the object to be compared is an instance of
     *         <code>MapPoint</code> and has the same values; <code>false</code> otherwise
     */

    public boolean equals(Object obj) {
        if (obj instanceof MapPoint) {
            MapPoint pt = (MapPoint) obj;
            return (lat == pt.lat) && (lon == pt.lon);
        }
        return super.equals(obj);
    }

    public int compareTo(Object o) {
        MapPoint other = (MapPoint) o;
        double dx = this.lon - other.lon;
        if (dx == 0.0) {
            dx = this.lat - other.lat;
        }
        return (int) dx;
    }

    public double getDistance0(MapPoint pt) {

        double lat0r = Math.toRadians(lat);
        double lat1r = Math.toRadians(pt.lat);
        double lon0r = Math.toRadians(lon);
        double lon1r = Math.toRadians(pt.lon);

        double dlat = Math.sin((lat1r - lat0r) / 2.0);
        double dlon = Math.sin((lon1r - lon0r) / 2.0);

        return Math.toDegrees(Math.asin(Math.sqrt(dlat * dlat + Math.cos(lat1r) * Math.cos(lat0r) * dlon * dlon)));
    }

    private double abs(double value) {
        return (value < 0.0) ? -value : value;
    }

    public boolean isOutsideLimit() {
        boolean inRange = true;
        if (lat > 90.0 || lat < -90.0) {
            inRange = false;
        }
        if (lon > 180.0 || lon < -180.0) {
            inRange = false;
            lon = ((lon - 180.0) % 360.) + 180.0;
        }
        return inRange;
    }

    private boolean sameSign(double value1, double value2) {
        if (value1 >= 0.0 && value2 >= 0.0)
            return true;
        if (value1 <= 0.0 && value2 <= 0.0)
            return true;
        return false;
    }

    public boolean isSame(MapPoint pt1, double resolution) {
        if (pt1 == null)
            return false;
        double dlat = abs(pt1.lat - lat);
        if (dlat > resolution)
            return false;
        double dlon = abs(pt1.lon - lon);
        if (dlon < resolution) {
            return true;
        }
        if (sameSign(pt1.lon, lon) == false)
            return false;

        double dist = abs(getDistance0(pt1));
        return (dist < resolution);
    }

    /**
     * Returns a string representation of this point and its location in the
     * (<i>lat</i>,&nbsp;<i>lon</i>) coordinate space. This method is intended to be used only for
     * debugging purposes, and the content and format of the returned string may vary between
     * implementations. The returned string may be empty but may not be <code>null</code>.
     * 
     * @return a string representation of this point
     */

    public String toString() {
        return "[lat=" + lat + ",lon=" + lon + "]";
    }
}
