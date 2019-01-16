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

import com.viper.projections.LambertConformalConic;
import com.viper.projections.MapPoint;

public class TestLambertConformalConicCase extends Assert {

	LambertConformalConic lcc = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		lcc = new LambertConformalConic();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #15 Lambert Conformal Conic (Forward Ellipse) Page 296-297
	// --------------------------------------------------------------
	@Test
	public void testLambertConformalConicSphereForward() {

		lcc.setOriginLat(23.0);
		lcc.setOriginLon(-96.0);
		lcc.setParallel1(33.00);
		lcc.setParallel2(45.00);
		lcc.setEccentricity(0.0);
		lcc.setRadius(1.0);

		value.lat = 35.0;
		value.lon = -75.0;

		lcc.toProjection(value, answer);

		assertEquals(0.2462112, answer.lat, ACCURRACY);
		assertEquals(0.2966785, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #15 LambertConformalConic (Inverse Sphere)
	// --------------------------------------------------------------
	@Test
	public void testLambertConformalConicSphereInverse() {

		lcc.setOriginLat(23.0);
		lcc.setOriginLon(-96.0);
		lcc.setParallel1(33.00);
		lcc.setParallel2(45.00);
		lcc.setEccentricity(0.0);
		lcc.setRadius(1.0);

		value.lat = 0.2462112;
		value.lon = 0.2966785;

		lcc.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #15 LambertConformalConic (Forward Ellipsoid)
	// --------------------------------------------------------------
	@Test
	public void testLambertConformalConicEllipsoidForward() {

		lcc.setOriginLat(23.0);
		lcc.setOriginLon(-96.0);
		lcc.setParallel1(33.00);
		lcc.setParallel2(45.00);
		lcc.setEccentricity(lcc.EarthEccentricity);
		lcc.setRadius(lcc.EquatorialRadius);

		value.lat = 35.0;
		value.lon = -75.0;

		lcc.toProjection(value, answer);

		assertEquals(1564649.472095699, answer.lat, ACCURRACY);
		assertEquals(1894410.900736346, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #15 LambertConformalConic (Inverse Ellipsoid)
	// --------------------------------------------------------------

	@Test
	public void testLambertConformalConicEllipsoidInverse() {

		lcc.setOriginLat(23.0);
		lcc.setOriginLon(-96.0);
		lcc.setParallel1(33.00);
		lcc.setParallel2(45.00);
		lcc.setEccentricity(lcc.EarthEccentricity);
		lcc.setRadius(lcc.EquatorialRadius);

		value.lat = 1564649.5;
		value.lon = 1894419.9;

		lcc.toLatLon(value, answer);

		assertEquals(34.9991687, answer.lat, ACCURRACY);
		assertEquals(-75.0000013, answer.lon, ACCURRACY);
	}
}
