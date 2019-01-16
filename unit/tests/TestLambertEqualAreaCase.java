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
import com.viper.projections.LambertEqualArea;
import com.viper.projections.MapPoint;

public class TestLambertEqualAreaCase extends Assert {

	LambertEqualArea azimuthal = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;
	double ACCURRACY_DEG = 0.1;

	@Before
	public void setUp() {
		azimuthal = new LambertEqualArea();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (ComputeQ)
	// --------------------------------------------------------------
	@Test
	public void testLambertComputeQ() {

		double E = azimuthal.EarthEccentricity;

		double lat = 30.0 * azimuthal.toRadians;

		double answer = azimuthal.computeQ(lat, E);

		assertEquals(0.9943535, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (ComputeQ)
	// --------------------------------------------------------------

	@Test
	public void testLambertComputeQ2() {

		double E = azimuthal.EarthEccentricity;

		double lat = 40.0 * azimuthal.toRadians;

		double answer = azimuthal.computeQ(lat, E);

		assertEquals(1.2792602, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (Forward Sphere) Page 332
	// --------------------------------------------------------------

	@Test
	public void testLambertSphereForward() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setOriginLat(90.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(3.0);

		value.lat = -20.0;
		value.lon = 100.0;

		azimuthal.toProjection(value, answer);

		assertEquals(-4.2339303, answer.lon, ACCURRACY);
		assertEquals(4.0257775, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (Inverse Sphere) Page 332-333
	// --------------------------------------------------------------

	@Test
	public void testLambertSphereInverse() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setOriginLat(90.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(3.0);

		value.lat = 4.0257775;
		value.lon = -4.2339303;

		azimuthal.toLatLon(value, answer);

		assertEquals(-20.0, answer.lat, ACCURRACY);
		assertEquals(100.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (Forward Ellipse) Page 338-341
	// --------------------------------------------------------------

	@Test
	public void testLambertEllipsoidForward() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setOriginLat(90.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(azimuthal.EarthEccentricity);
		azimuthal.setRadius(azimuthal.EquatorialRadius);

		value.lat = 30.0;
		value.lon = -110.0;

		azimuthal.toProjection(value, answer);

		assertEquals(-1056814.9225206971, answer.lat, ACCURRACY);
		assertEquals(-965932.111123986, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (Inverse Ellipse) Page 341-344
	// --------------------------------------------------------------

	@Test
	public void testLambertEllipsoidInverse() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setOriginLat(90.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(azimuthal.EarthEccentricity);
		azimuthal.setRadius(azimuthal.EquatorialRadius);

		value.lat = -1056814.9225206971;
		value.lon = -965932.111123986;

		azimuthal.toLatLon(value, answer);

		assertEquals(30.0, answer.lat, ACCURRACY);
		assertEquals(-110.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Lambert Equal Area (Stress)
	// --------------------------------------------------------------

	@Test
	public void testLambertStress() {

		DataList list = DataList.createInstance();

		list.add(90.0, 0.0, 1.41421, 0.0);
		list.add(80.0, 0.0, 1.28558, 0.0);
		list.add(70.0, 0.0, 1.14715, 0.0);
		list.add(60.0, 0.0, 1.00000, 0.0);
		list.add(50.0, 0.0, 0.84524, 0.0);
		list.add(40.0, 0.0, 0.68404, 0.0);
		list.add(30.0, 0.0, 0.51764, 0.0);
		list.add(20.0, 0.0, 0.34730, 0.0);
		list.add(10.0, 0.0, 0.17431, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);

		list.add(90.0, 10.0, 1.41421, 0.00000);
		list.add(80.0, 10.0, 1.28702, 0.03941);
		list.add(70.0, 10.0, 1.14938, 0.07264);
		list.add(60.0, 10.0, 1.00254, 0.10051);
		list.add(50.0, 10.0, 0.84776, 0.12353);

		list.add(80.0, 20.0, 1.29135, 0.07788);

		list.add(80.0, 30.0, 1.29851, 0.11448);

		azimuthal.setOriginLon(0.0);
		azimuthal.setOriginLat(90.0);
		azimuthal.setParallel1(0.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			azimuthal.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
	}
}
