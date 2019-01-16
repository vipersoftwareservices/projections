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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.viper.projections.DataList;
import com.viper.projections.MapPoint;
import com.viper.projections.Mercator;

public class TestMercatorCase extends Assert {

	Mercator mercator = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		mercator = new Mercator();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #7 Mercator (Sphere Forward) Page 266
	// --------------------------------------------------------------
	@Test
	public void testMercatorSphereForward() {

		mercator.setOriginLon(-180.0);
		mercator.setEccentricity(0.0); // Indicates Spherical System
		mercator.setRadius(1.0);

		answer.lat = 0.0;
		answer.lon = 0.0;
		value.lon = -75.0;
		value.lat = 35.0;

		mercator.toProjection(value, answer);

		assertEquals(0.6528366, answer.lat, ACCURRACY);
		assertEquals(1.8325957, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #7 Mercator (Sphere Inverse) Page 267
	// --------------------------------------------------------------
	@Test
	public void testMercatorSphereInverse() {

		mercator.setOriginLon(-180.0);
		mercator.setEccentricity(0.0); // Indicates Spherical System
		mercator.setRadius(1.0);

		answer.lat = 0.0;
		answer.lon = 0.0;
		value.lon = 1.8325957;
		value.lat = 0.6528366;

		mercator.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #7 Mercator (Forward Ellipse) Page 267
	// --------------------------------------------------------------
	@Test
	public void testMercatorEllipseForward() {

		mercator.setOriginLon(-180.0);
		mercator.setEccentricity(mercator.EarthEccentricity);
		mercator.setRadius(mercator.EquatorialRadius);

		answer.lat = 0.0;
		answer.lon = 0.0;
		value.lon = -75.0;
		value.lat = 35.0;

		mercator.toProjection(value, answer);

		assertEquals(11688673.71544, answer.lon, ACCURRACY);
		assertEquals(4139145.635, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #7 Mercator (Inverse Ellipse) Page 267-268
	// --------------------------------------------------------------
	@Test
	public void testMercatorEllipseInverse() {

		mercator.setOriginLon(-180.0);
		mercator.setEccentricity(mercator.EarthEccentricity);
		mercator.setRadius(mercator.EquatorialRadius);

		answer.lat = 0.0;
		answer.lon = 0.0;
		value.lon = 11688673.71544;
		value.lat = 4139145.635;

		mercator.toLatLon(value, answer);

		assertEquals(-75.0, answer.lon, ACCURRACY);
		assertEquals(35.0, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #Stress Mercator Spherical (Forward and Inverse)
	// --------------------------------------------------------------
	@Test
	public void testMercatorSphericalStress1() {

		DataList list = DataList.createInstance();
		list.add(85.0, 0.0, 179.410, 0.0);
		list.add(80.0, 0.0, 139.586, 0.0);
		list.add(75.0, 0.0, 116.172, 0.0);
		list.add(70.0, 0.0, 99.4322, 0.0);
		list.add(65.0, 0.0, 86.3132, 0.0);
		list.add(60.0, 0.0, 75.4562, 0.0);
		list.add(55.0, 0.0, 66.1325, 0.0);
		list.add(50.0, 0.0, 57.9076, 0.0);
		list.add(45.0, 0.0, 50.4987, 0.0);
		list.add(40.0, 0.0, 43.7115, 0.0);
		list.add(35.0, 0.0, 37.4049, 0.0);
		list.add(30.0, 0.0, 31.4731, 0.0);
		list.add(25.0, 0.0, 25.8335, 0.0);
		list.add(20.0, 0.0, 20.4190, 0.0);
		list.add(15.0, 0.0, 15.1742, 0.0);
		list.add(10.0, 0.0, 10.0514, 0.0);
		list.add(5.0, 0.0, 5.00650, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);
		list.add(89.0, 180.0, 271.65900, 180.0);
		list.add(89.99, 180.0, 535.51754, 180.0);
		list.add(-89.0, 180.0, -271.65900, 180.0);
		list.add(-85.0, 0.0, -179.410, 0.0);
		list.add(-80.0, 0.0, -139.586, 0.0);
		list.add(-75.0, 0.0, -116.172, 0.0);
		list.add(-70.0, 0.0, -99.4322, 0.0);
		list.add(-65.0, 0.0, -86.3132, 0.0);
		list.add(-60.0, 0.0, -75.4562, 0.0);
		list.add(-55.0, 0.0, -66.1325, 0.0);
		list.add(-50.0, 0.0, -57.9076, 0.0);
		list.add(-45.0, 0.0, -50.4987, 0.0);
		list.add(-40.0, 0.0, -43.7115, 0.0);
		list.add(-35.0, 0.0, -37.4049, 0.0);
		list.add(-30.0, 0.0, -31.4731, 0.0);
		list.add(-25.0, 0.0, -25.8335, 0.0);
		list.add(-20.0, 0.0, -20.4190, 0.0);
		list.add(-15.0, 0.0, -15.1742, 0.0);
		list.add(-10.0, 0.0, -10.0514, 0.0);
		list.add(-5.0, 0.0, -5.00650, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);
		list.add(35.0, 75.0, 37.4049, 75.0);
		list.add(-35.0, 75.0, -37.4049, 75.0);
		list.add(35.0, -75.0, 37.4049, -75.0);
		list.add(-35.0, -75.0, -37.4049, -75.0);
		list.add(0.0, 0.0, 0.00000, 0.0);

		MapPoint answer = new MapPoint();
		Mercator mercator = new Mercator();
		mercator.setOriginLon(0.0);
		mercator.setEccentricity(0.0);
		mercator.setRadius(mercator.toDegrees);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toLatLon(pp, answer);

			assertEquals("inverse lat, pp=" + pp.toString(), mp.lat, answer.lat, ACCURRACY);
			assertEquals("inverse lon, pp=" + pp.toString(), mp.lon, answer.lon, ACCURRACY);
		}
	}

	// --------------------------------------------------------------
	// #Stress Mercator Spherical (Forward and Inverse)
	// See page 45, Table 7. Mercator tests sphere (R = 1, E = 0);
	// --------------------------------------------------------------
	@Test
	public void testMercatorSphericalStress2() {

		DataList list = DataList.createInstance();
		list.add(85.0, 0.0, 3.13130, 0.0);
		list.add(80.0, 0.0, 2.43625, 0.0);
		list.add(75.0, 0.0, 2.02759, 0.0);
		list.add(70.0, 0.0, 1.73542, 0.0);
		list.add(65.0, 0.0, 1.50645, 0.0);
		list.add(60.0, 0.0, 1.31696, 0.0);
		list.add(55.0, 0.0, 1.15423, 0.0);
		list.add(50.0, 0.0, 1.01068, 0.0);
		list.add(45.0, 0.0, 0.88137, 0.0);
		list.add(40.0, 0.0, 0.76291, 0.0);
		list.add(35.0, 0.0, 0.65284, 0.0);
		list.add(30.0, 0.0, 0.54931, 0.0);
		list.add(25.0, 0.0, 0.45088, 0.0);
		list.add(20.0, 0.0, 0.35638, 0.0);
		list.add(15.0, 0.0, 0.26484, 0.0);
		list.add(10.0, 0.0, 0.17543, 0.0);
		list.add(5.0, 0.0, 0.08738, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);
		list.add(-85.0, 0.0, -3.13130, 0.0);
		list.add(-80.0, 0.0, -2.43625, 0.0);
		list.add(-75.0, 0.0, -2.02759, 0.0);
		list.add(-70.0, 0.0, -1.73542, 0.0);
		list.add(-65.0, 0.0, -1.50645, 0.0);
		list.add(-60.0, 0.0, -1.31696, 0.0);
		list.add(-55.0, 0.0, -1.15423, 0.0);
		list.add(-50.0, 0.0, -1.01068, 0.0);
		list.add(-45.0, 0.0, -0.88137, 0.0);
		list.add(-40.0, 0.0, -0.76291, 0.0);
		list.add(-35.0, 0.0, -0.65284, 0.0);
		list.add(-30.0, 0.0, -0.54931, 0.0);
		list.add(-25.0, 0.0, -0.45088, 0.0);
		list.add(-20.0, 0.0, -0.35638, 0.0);
		list.add(-15.0, 0.0, -0.26484, 0.0);
		list.add(-10.0, 0.0, -0.17543, 0.0);
		list.add(-5.0, 0.0, -0.08738, 0.0);

		MapPoint answer = new MapPoint();
		Mercator mercator = new Mercator();
		mercator.setOriginLon(0.0);
		mercator.setEccentricity(0.0);
		mercator.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toLatLon(pp, answer);

			assertEquals("inverse lat, pp=" + pp.toString(), mp.lat, answer.lat, ACCURRACY);
			assertEquals("inverse lon, pp=" + pp.toString(), mp.lon, answer.lon, ACCURRACY);
		}
	}

	// --------------------------------------------------------------
	// #Stress Mercator Ellipsoid (Forward and Inverse)
	// See page 45, Table 7.
	// Mercator tests ellipse (R = 1, E = EarthEccentricity);
	// --------------------------------------------------------------
	@Test
	public void testMercatorSphericalStress3() {

		DataList list = DataList.createInstance();
		list.add(85.0, 0.0, 3.12454, 0.0);
		list.add(80.0, 0.0, 2.42957, 0.0);
		list.add(75.0, 0.0, 2.02104, 0.0);
		list.add(70.0, 0.0, 1.72904, 0.0);
		list.add(65.0, 0.0, 1.50031, 0.0);
		list.add(60.0, 0.0, 1.31109, 0.0);
		list.add(55.0, 0.0, 1.14868, 0.0);
		list.add(50.0, 0.0, 1.00549, 0.0);
		list.add(45.0, 0.0, 0.87658, 0.0);
		list.add(40.0, 0.0, 0.75855, 0.0);
		list.add(35.0, 0.0, 0.64895, 0.0);
		list.add(30.0, 0.0, 0.54592, 0.0);
		list.add(25.0, 0.0, 0.44801, 0.0);
		list.add(20.0, 0.0, 0.35406, 0.0);
		list.add(15.0, 0.0, 0.26309, 0.0);
		list.add(10.0, 0.0, 0.17425, 0.0);
		list.add(5.0, 0.0, 0.08679, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);
		list.add(-85.0, 0.0, -3.12454, 0.0);
		list.add(-80.0, 0.0, -2.42957, 0.0);
		list.add(-75.0, 0.0, -2.02104, 0.0);
		list.add(-70.0, 0.0, -1.72904, 0.0);
		list.add(-65.0, 0.0, -1.50031, 0.0);
		list.add(-60.0, 0.0, -1.31109, 0.0);
		list.add(-55.0, 0.0, -1.14868, 0.0);
		list.add(-50.0, 0.0, -1.00549, 0.0);
		list.add(-45.0, 0.0, -0.87658, 0.0);
		list.add(-40.0, 0.0, -0.75855, 0.0);
		list.add(-35.0, 0.0, -0.64895, 0.0);
		list.add(-30.0, 0.0, -0.54592, 0.0);
		list.add(-25.0, 0.0, -0.44801, 0.0);
		list.add(-20.0, 0.0, -0.35406, 0.0);
		list.add(-15.0, 0.0, -0.26309, 0.0);
		list.add(-10.0, 0.0, -0.17425, 0.0);
		list.add(-5.0, 0.0, -0.08679, 0.0);

		MapPoint answer = new MapPoint();
		Mercator mercator = new Mercator();
		mercator.setOriginLon(0.0);
		mercator.setEccentricity(mercator.EarthEccentricity);
		mercator.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mercator.toLatLon(pp, answer);

			assertEquals("inverse lat, pp=" + pp.toString(), mp.lat, answer.lat, ACCURRACY);
			assertEquals("inverse lon, pp=" + pp.toString(), mp.lon, answer.lon, ACCURRACY);
		}
	}

	// --------------------------------------------------------------
	// MapProjection (getAzimuth)
	// --------------------------------------------------------------
	@Test
	public void testMapProjectionMercator1() {

		MapPoint mp1 = new MapPoint();
		MapPoint mp2 = new MapPoint();

		mp1.lat = 40.0;
		mp1.lon = 40.0;

		mp2.lat = 50.0;
		mp2.lon = 50.0;

		double answer = mercator.getAzimuth(mp1, mp2);

		assertEquals(31.813917741055, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// MapProjection (getAzimuth)
	// --------------------------------------------------------------
	@Test
	public void testMapProjectionMercator2() {

		MapPoint mp1 = new MapPoint();
		MapPoint mp2 = new MapPoint();

		mp1.lat = 40.0;
		mp1.lon = 40.0;

		mp2.lat = 40.0;
		mp2.lon = 50.0;

		double answer = mercator.getAzimuth(mp1, mp2);

		assertEquals(86.78126879480764, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// MapProjection (getAzimuth)
	// --------------------------------------------------------------
	@Test
	public void testMapProjectionMercator3() {

		MapPoint mp1 = new MapPoint();
		MapPoint mp2 = new MapPoint();

		mp1.lat = 40.0;
		mp1.lon = 40.0;

		mp2.lat = 40.0;
		mp2.lon = 30.0;

		double answer = mercator.getAzimuth(mp1, mp2);

		assertEquals(-86.78126879480764, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// MapProjection (getAzimuth)
	// --------------------------------------------------------------
	@Test
	public void testMapProjectionMercator4() {

		MapPoint mp1 = new MapPoint();
		MapPoint mp2 = new MapPoint();

		mp1.lat = 40.0;
		mp1.lon = 40.0;

		mp2.lat = 30.0;
		mp2.lon = 40.0;

		double answer = mercator.getAzimuth(mp1, mp2);

		assertEquals(180.0, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// MapProjection (getAzimuth)
	// --------------------------------------------------------------
	@Test
	public void testMapProjectionMercator5() {

		MapPoint mp1 = new MapPoint();
		MapPoint mp2 = new MapPoint();

		mp1.lat = 40.0;
		mp1.lon = 40.0;

		mp2.lat = 50.0;
		mp2.lon = 40.0;

		double answer = mercator.getAzimuth(mp1, mp2);

		assertEquals(0.0, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #7 Mercator (getDistance)
	// --------------------------------------------------------------
	@Test
	public void testMercatorDistance() {

		mercator.setOriginLon(0.0);
		mercator.setEccentricity(mercator.EarthEccentricity);
		mercator.setRadius(1.0);

		MapPoint pt1 = new MapPoint(0.0, 30.0);
		MapPoint pt2 = new MapPoint(0.0, 31.0);

		double answer = mercator.getDistance(pt1, pt2);

		assertEquals(0.0174532925199433, answer, ACCURRACY);
	}
}
