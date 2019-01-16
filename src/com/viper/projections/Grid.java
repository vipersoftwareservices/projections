/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2003 
 *
 * @note 
 *        
 * ---------------------------------------------------------------
 */

package com.viper.projections;

public class Grid implements java.io.Serializable {

    private double east;
    private double west;
    private double north;
    private double south;

    public Grid() {
        this(0, 0, 0, 0);
    }

    public Grid(double east, double west, double north, double south) {
        this.east = adjustLon(east);
        this.west = adjustLon(west);
        this.north = adjustLat(north);
        this.south = adjustLat(south);
    }

    public void reset(double east, double west, double north, double south) {
        this.east = adjustLon(east);
        this.west = adjustLon(west);
        this.north = adjustLat(north);
        this.south = adjustLat(south);
    }

    public double adjustLat(double lat) {
        return ((lat + 90.0) % 180.0) - 90.0;
    }

    public double adjustLon(double lon) {
        return ((lon + 180.0) % 360.0) - 180.0;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public boolean containsLat(double lat) {
        double Y = adjustLat(lat);
        return (Y <= north && Y >= south);
    }

    public boolean containsLon(double lon) {
        double X = adjustLon(lon);
        return (X <= east && X >= west);
    }

    public boolean contains(MapPoint pt) {
        return containsLat(pt.lat) && containsLon(pt.lon);
    }

    public boolean contains(double lat, double lon) {
        return containsLat(lat) && containsLon(lon);
    }

    public boolean contains(Grid r) {
        return contains(r.east, r.west, r.north, r.south);
    }

    public boolean contains(double east, double west, double north, double south) {

        double E = adjustLon(east);
        double W = adjustLon(west);
        double N = adjustLat(north);
        double S = adjustLat(south);

        return ((E <= east && E >= west) || (W <= east && W >= west)) &&
               ((N <= north && N >= south) || (S <= north && S >= south));
    }

    public boolean intersects(Grid r) {

        // TBD

        return false;
    }

    public Grid intersection(Grid r) {

        // TBD
        return r;
    }

    public Grid union(Grid r) {
        double W = Math.min(west, r.west);
        double E = Math.max(east, r.east);
        double N = Math.max(north, r.north);
        double S = Math.min(south, r.south);

        return new Grid(E, W, N, S);
    }

    public void add(double lat, double lon) {
        west = Math.min(lon, west);
        east = Math.max(lon, east);
        north = Math.max(lat, north);
        south = Math.min(lat, south);
    }

    public void add(MapPoint pt) {
        add(pt.lat, pt.lon);
    }

    public void add(Grid r) {
        west = Math.min(west, r.west);
        east = Math.max(east, r.east);
        north = Math.max(north, r.north);
        south = Math.min(south, r.south);
    }

    public boolean isEmpty() {
        return (west == east) || (north == south);
    }

    public void grow(double south, double north, double west, double east) {
        this.south = (south < this.south) ? south : this.south;
        this.north = (north > this.north) ? north : this.north;
        this.west = (west < this.west) ? west : this.west;
        this.east = (east > this.east) ? east : this.east;
    }

    public void grow(Grid g) {
        this.south = (g.south < this.south) ? g.south : this.south;
        this.north = (g.north > this.north) ? g.north : this.north;
        this.west = (g.west < this.west) ? g.west : this.west;
        this.east = (g.east > this.east) ? g.east : this.east;
    }

    public void grow(MapPoint pt) {
        this.south = (pt.lat < this.south) ? pt.lat : this.south;
        this.north = (pt.lat > this.north) ? pt.lat : this.north;
        this.west = (pt.lon < this.west) ? pt.lon : this.west;
        this.east = (pt.lon > this.east) ? pt.lon : this.east;
    }

    public void grow(double lat, double lon) {
        this.south = (lat < this.south) ? lat : this.south;
        this.north = (lat > this.north) ? lat : this.north;
        this.west = (lon < this.west) ? lon : this.west;
        this.east = (lon > this.east) ? lon : this.east;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Grid)) {
            return super.equals(obj);
        }

        Grid r = (Grid) obj;

        return (east == r.east && west == r.west && south == r.south && north == r.north);
    }

    public String toString() {
        return getClass().getName() + "[east=" + east + ",west=" + west + ",north=" + north + ",south=" + south + "]";
    }
}
