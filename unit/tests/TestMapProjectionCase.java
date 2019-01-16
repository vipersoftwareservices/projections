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

package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.viper.projections.MapPoint;
import com.viper.projections.Passthru;

public class TestMapProjectionCase extends Assert {

    Passthru MP = null;
    MapPoint value = null;
    MapPoint expected = null;

    double ACCURRACY = 0.001;

    @Before
    public void setUp() {
        MP = new Passthru();
        value = new MapPoint();
        expected = new MapPoint();
    }

    // --------------------------------------------------------------
    // MapProjection (getAzimuth)
    // --------------------------------------------------------------
    @Test
    public void testMapProjectionAzimuth1() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = 10.0;
        mp2.lon = 10.0;

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(44.56145141325769, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getAzimuth)
    // --------------------------------------------------------------
    @Test
    public void testMapProjectionAzimuth2() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = 0.0;
        mp2.lon = 10.0;

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(90.0, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getAzimuth)
    // --------------------------------------------------------------
    @Test
    public void testMapProjectionAzimuth3() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = 0.0;
        mp2.lon = -10.0;

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(-90.0, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getAzimuth)
    // --------------------------------------------------------------
    @Test
    public void testMapProjectionAzimuth4() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = 10.0;
        mp2.lon = 0.0;

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(0.0, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getAzimuth)
    // --------------------------------------------------------------
    @Test
    public void testMapProjectionAzimuth5() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = -10.0;
        mp2.lon = 0.0;

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(180.0, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getDistance)
    // --------------------------------------------------------------
    @Test
    public void testGetDistance1() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(MP.EquatorialRadius);

        mp1.lat = 0.0;
        mp1.lon = 1.0;

        mp2.lat = 0.0;
        mp2.lon = 0.0;

        double answer = MP.getDistance(mp1, mp2);

        assertEquals(111320.70205177447, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getDistance)
    // --------------------------------------------------------------
    @Test
    public void testGetDistance1A() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(180.0 * 60.0 / MP.PI);

        mp1.lat = 33.0 + 57.0 / 60.0;
        mp1.lon = -(118.0 + 24.0 / 60.0);

        mp2.lat = 40.0 + 38.0 / 60.0;
        mp2.lon = -(73.0 + 47.0 / 60.0);

        double answer = MP.getDistance(mp1, mp2);

        assertEquals(2143.7261012545, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getDistance)
    // --------------------------------------------------------------
    @Test
    public void testGetDistance2A() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(180.0 * 60.0 / MP.PI);

        mp1.lat = 33.0 + 57.0 / 60.0;
        mp1.lon = -(118.0 + 24.0 / 60.0);

        mp2.lat = 40.0 + 38.0 / 60.0;
        mp2.lon = -(73.0 + 47.0 / 60.0);

        double answer = MP.getDistanceGreatCircle2(mp1, mp2);

        assertEquals(2143.7261012545, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getDistance)
    // --------------------------------------------------------------
    @Test
    public void testGetAzimuth2() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(180.0 * 60.0 / MP.PI);

        mp1.lat = 33.0 + 57.0 / 60.0;
        mp1.lon = -(118.0 + 24.0 / 60.0);

        mp2.lat = 40.0 + 38.0 / 60.0;
        mp2.lon = -(73.0 + 47.0 / 60.0);

        double answer = MP.getAzimuth(mp1, mp2);

        assertEquals(65.89216655274532, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getDistance3A)
    // --------------------------------------------------------------
    @Test
    public void testGetDistance3A() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();

        MP.setRadius(MP.toDegrees);

        mp1.lat = 165.693;
        mp1.lon = -180.0;

        mp2.lat = 165.693;
        mp2.lon = 180.0;

        double answer = MP.getDistance(mp1, mp2);

        assertEquals(1.0, answer, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getLatLon2)
    // --------------------------------------------------------------
    @Test
    public void testGetLatLon2GeneralForm() {

        MapPoint mp1 = new MapPoint();
        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(MP.EquatorialRadius);

        mp1.lat = 0.0;
        mp1.lon = 0.0;
        double distance = 111320.70205177447;
        double bearing = -90.0;

        MapPoint answer = MP.toLatLon2GeneralForm(mp1, distance, bearing);

        assertEquals(0.0, answer.lat, ACCURRACY);
        assertEquals(-1.0, answer.lon, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (getLatLon2)
    // --------------------------------------------------------------
    @Test
    public void testGetLatLon2LAX() {

        MapPoint mp1 = new MapPoint();

        MP.setEccentricity(MP.EarthEccentricity);
        MP.setRadius(180.0 * 60.0 / MP.PI);

        mp1.lat = 33.0 + 57.0 / 60.0;
        mp1.lon = -(118.0 + 24.0 / 60.0);

        double distance = 2143.7261012545;
        double bearing = 65.89216655274532;

        MapPoint answer = MP.toLatLon2GeneralForm(mp1, distance, bearing);

        assertEquals((40.0 + 38.0 / 60.0), answer.lat, ACCURRACY);
        assertEquals(-(73.0 + 47.0 / 60.0), answer.lon, ACCURRACY);
    }

    // --------------------------------------------------------------
    // MapProjection (computeRhumbLine)
    // --------------------------------------------------------------
    @Test
    public void testComputeRhumbLine() {

        MapPoint mp1 = new MapPoint();
        MapPoint mp2 = new MapPoint();
        MP.setEccentricity(0.0);
        MP.setRadius(1.0);

        mp1.lat = 0.0;
        mp1.lon = 0.0;

        mp2.lat = 1.0;
        mp2.lon = 1.0;

        List<MapPoint> pts = new ArrayList<MapPoint>();

        MP.computeRhumbLine(pts, mp1, mp2, 20);

        assertEquals(0.00000, getLat(pts, 0), ACCURRACY);
        assertEquals(0.00000, getLon(pts, 0), ACCURRACY);
        assertEquals(0.10000, getLat(pts, 1), ACCURRACY);
        assertEquals(0.09999, getLon(pts, 1), ACCURRACY);
        assertEquals(0.20000, getLat(pts, 2), ACCURRACY);
        assertEquals(0.19999, getLon(pts, 2), ACCURRACY);
        assertEquals(0.30000, getLat(pts, 3), ACCURRACY);
        assertEquals(0.29999, getLon(pts, 3), ACCURRACY);
        assertEquals(0.40000, getLat(pts, 4), ACCURRACY);
        assertEquals(0.39999, getLon(pts, 4), ACCURRACY);
        assertEquals(0.50000, getLat(pts, 5), ACCURRACY);
        assertEquals(0.49999, getLon(pts, 5), ACCURRACY);
        assertEquals(0.60000, getLat(pts, 6), ACCURRACY);
        assertEquals(0.59999, getLon(pts, 6), ACCURRACY);
        assertEquals(0.70000, getLat(pts, 7), ACCURRACY);
        assertEquals(0.69999, getLon(pts, 7), ACCURRACY);
        assertEquals(0.80000, getLat(pts, 8), ACCURRACY);
        assertEquals(0.79999, getLon(pts, 8), ACCURRACY);
        assertEquals(0.90000, getLat(pts, 9), ACCURRACY);
        assertEquals(0.89999, getLon(pts, 9), ACCURRACY);
        assertEquals(1.00000, getLat(pts, 10), ACCURRACY);
        assertEquals(1.00000, getLon(pts, 10), ACCURRACY);
    }

    public double getLat(List<MapPoint> list, int index) {
        MapPoint pt = list.get(index);
        return pt.lat;
    }

    public double getLon(List<MapPoint> list, int index) {
        MapPoint pt = list.get(index);
        return pt.lon;
    }
}
