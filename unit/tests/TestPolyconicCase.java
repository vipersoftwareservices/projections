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

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.viper.projections.MapPoint;
import com.viper.projections.Polyconic;

public class TestPolyconicCase extends TestCase {

	Polyconic polyconic = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		polyconic = new Polyconic();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #11 Polyconic (computeM)
	// --------------------------------------------------------------
	@Test
	public void testPolyconicM() {

		double lat = 40.0 * polyconic.toRadians;
		double E = polyconic.EarthEccentricity;
		double a = polyconic.EquatorialRadius;

		double answer = a * polyconic.computeM(lat, E);

		assertEquals(4429318.881887786, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (computeM)
	// --------------------------------------------------------------
	@Test
	public void testPolyconicM0() {

		double lat = 30.0 * polyconic.toRadians;
		double E = polyconic.EarthEccentricity;
		double a = polyconic.EquatorialRadius;

		double answer = a * polyconic.computeM(lat, E);

		assertEquals(3319933.2772094775, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (computeM)
	// --------------------------------------------------------------
	@Test
	public void testPolyconicMP() {

		double lat = 0.7274131;
		double E = polyconic.EarthEccentricity;
		double a = polyconic.EquatorialRadius;

		double answer = polyconic.computeMP(lat, E);

		assertEquals(0.9977068, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (Forward Sphere) Page 287
	// --------------------------------------------------------------
	@Test
	public void testPolyconicSphereForward() {

		polyconic.setOriginLat(30.0);
		polyconic.setOriginLon(-96.0);
		polyconic.setEccentricity(0.0);
		polyconic.setRadius(1.0);

		value.lat = 40.0;
		value.lon = -75.0;

		polyconic.toProjection(value, answer);

		assertEquals(0.2074541, answer.lat, ACCURRACY);
		assertEquals(0.2781798, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (Inverse Sphere) Page 287-288
	// --------------------------------------------------------------
	@Test
	public void testPolyconicSphereInverse() {

		polyconic.setOriginLat(30.0);
		polyconic.setOriginLon(-96.0);
		polyconic.setEccentricity(0.0);
		polyconic.setRadius(1.0);

		value.lat = 0.2074541;
		value.lon = 0.2781798;

		polyconic.toLatLon(value, answer);

		assertEquals(40.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (Forward Ellipsoid) Page 287
	// --------------------------------------------------------------
	@Test
	public void testPolyconicEllipsoidForward() {

		polyconic.setOriginLat(30.0);
		polyconic.setOriginLon(-96.0);
		polyconic.setEccentricity(polyconic.EarthEccentricity);
		polyconic.setRadius(polyconic.EquatorialRadius);

		value.lat = 40.0;
		value.lon = -75.0;

		polyconic.toProjection(value, answer);

		assertEquals(1319657.7721557359, answer.lat, ACCURRACY);
		assertEquals(1776774.5430117014, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Polyconic (Inverse Ellipsoid) Page 287-288
	// --------------------------------------------------------------
	@Test
	public void testPolyconicEllipsoidInverse() {

		polyconic.setOriginLat(30.0);
		polyconic.setOriginLon(-96.0);
		polyconic.setEccentricity(polyconic.EarthEccentricity);
		polyconic.setRadius(polyconic.EquatorialRadius);

		value.lat = 1319657.8;
		value.lon = 1776774.5;

		polyconic.toLatLon(value, answer);

		assertEquals(40.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}
}
