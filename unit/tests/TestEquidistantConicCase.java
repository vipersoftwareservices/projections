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

import com.viper.projections.EquidistantConic;
import com.viper.projections.MapPoint;

public class TestEquidistantConicCase extends Assert {

	EquidistantConic ec = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		ec = new EquidistantConic();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #16 EquidistantConic (Forward Sphere)
	// --------------------------------------------------------------
	@Test
	public void testEquidistantConicSphereForward() {

		ec.setOriginLat(23.0);
		ec.setOriginLon(-96.0);
		ec.setParallel1(29.50);
		ec.setParallel2(45.50);
		ec.setEccentricity(0.0);
		ec.setRadius(1.0);

		value.lat = 35.0;
		value.lon = -75.0;

		ec.toProjection(value, answer);

		assertEquals(0.2424021, answer.lat, ACCURRACY);
		assertEquals(0.2952057, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 EquidistantConic (Inverse Sphere)
	// --------------------------------------------------------------

	@Test
	public void testEquidistantConicSphereInverse() {

		ec.setOriginLat(23.0);
		ec.setOriginLon(-96.0);
		ec.setParallel1(29.50);
		ec.setParallel2(45.50);
		ec.setEccentricity(0.0);
		ec.setRadius(1.0);

		value.lat = 0.2424021;
		value.lon = 0.2952057;

		ec.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 EquidistantConic (Forward Ellipsoid)
	// --------------------------------------------------------------
	@Test
	public void testEquidistantConicEllipsoidForward() {

		ec.setOriginLat(23.0);
		ec.setOriginLon(-96.0);
		ec.setParallel1(29.50);
		ec.setParallel2(45.50);
		ec.setEccentricity(ec.EarthEccentricity);
		ec.setRadius(ec.EquatorialRadius);

		value.lat = 35.0;
		value.lon = -75.0;

		ec.toProjection(value, answer);

		assertEquals(1540507.6, answer.lat, ACCURRACY);
		assertEquals(1885051.9, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 EquidistantConic (Inverse Ellipsoid)
	// --------------------------------------------------------------

	@Test
	public void testEquidistantConicEllipsoidInverse() {

		ec.setOriginLat(23.0);
		ec.setOriginLon(-96.0);
		ec.setParallel1(29.50);
		ec.setParallel2(45.50);
		ec.setEccentricity(ec.EarthEccentricity);
		ec.setRadius(ec.EquatorialRadius);

		value.lat = 1540507.6;
		value.lon = 1885051.9;

		ec.toLatLon(value, answer);

		assertEquals(34.94678255340832, answer.lat, ACCURRACY);
		assertEquals(-74.99022440586026, answer.lon, ACCURRACY);
	}
}
