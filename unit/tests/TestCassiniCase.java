/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * 36710 Nichols Ave, Fremont CA, 94536
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

import com.viper.projections.Cassini;
import com.viper.projections.MapPoint;

public class TestCassiniCase extends Assert {

	Cassini cassini = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		cassini = new Cassini();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #11 Cassini (Forward Sphere) Page 287
	// --------------------------------------------------------------
	@Test
	public void testCassiniSphereForward() {

		cassini.setOriginLat(-20.0);
		cassini.setOriginLon(-75.0);
		cassini.setEccentricity(0.0);
		cassini.setRadius(1.0);

		value.lat = 25.0;
		value.lon = -90.0;

		cassini.toProjection(value, answer);

		assertEquals(0.7988243, answer.lat, ACCURRACY);
		assertEquals(-0.2367759, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Cassini (Inverse Sphere) Page 287-288
	// --------------------------------------------------------------

	@Test
	public void testCassiniSphereInverse() {

		cassini.setOriginLat(-20.0);
		cassini.setOriginLon(-75.0);
		cassini.setEccentricity(0.0);
		cassini.setRadius(1.0);

		value.lat = 0.7988243;
		value.lon = -0.2367759;

		cassini.toLatLon(value, answer);

		assertEquals(25.0, answer.lat, ACCURRACY);
		assertEquals(-90.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Cassini (Forward Ellipsoid) Page 287
	// --------------------------------------------------------------

	@Test
	public void testCassiniEllipsoidForward() {

		cassini.setOriginLat(40.0);
		cassini.setOriginLon(-75.0);
		cassini.setEccentricity(cassini.EarthEccentricity);
		cassini.setRadius(cassini.EquatorialRadius);

		value.lat = 43.0;
		value.lon = -73.0;

		cassini.toProjection(value, answer);

		assertEquals(335127.586707, answer.lat, ACCURRACY);
		assertEquals(163071.128172, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Cassini (Inverse Ellipsoid) Page 287-288
	// --------------------------------------------------------------

	@Test
	public void testCassiniEllipsoidInverse() {

		cassini.setOriginLat(40.0);
		cassini.setOriginLon(-75.0);
		cassini.setEccentricity(cassini.EarthEccentricity);
		cassini.setRadius(cassini.EquatorialRadius);

		value.lat = 335127.586707;
		value.lon = 163071.1;

		cassini.toLatLon(value, answer);

		assertEquals(42.976576, answer.lat, ACCURRACY);
		assertEquals(-73.000000, answer.lon, ACCURRACY);
	}
}
