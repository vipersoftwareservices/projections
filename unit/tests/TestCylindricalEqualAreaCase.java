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

import com.viper.projections.CylindricalEqualArea;
import com.viper.projections.MapPoint;

public class TestCylindricalEqualAreaCase extends Assert {

	CylindricalEqualArea cea = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		cea = new CylindricalEqualArea();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (computeK0)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaK0() {

		double lat1 = 5.0 * cea.toRadians;
		double E = cea.EarthEccentricity;

		double answer = cea.computeK0(lat1, E);

		assertEquals(0.9962203, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (computeQ)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaQ() {

		double lat1 = 5.0 * cea.toRadians;
		double E = cea.EarthEccentricity;

		double answer = cea.computeQ(lat1, E);

		assertEquals(0.1731376, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (computeT)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaT() {

		double B = 0.7004398;
		double E = cea.EarthEccentricity;

		double answer = cea.computeT(B, E);

		assertEquals(40.2761378 * cea.toRadians, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (Forward Sphere)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaSphereForward() {

		cea.setOriginLon(-75.0);
		cea.setParallel1(30.0);
		cea.setEccentricity(0.0);
		cea.setRadius(1.0);

		value.lat = 35.0;
		value.lon = 80.0;

		cea.toProjection(value, answer);

		assertEquals(0.6623090, answer.lat, ACCURRACY);
		assertEquals(2.3428242, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (Inverse Sphere)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaSphereInverse() {

		cea.setOriginLon(-75.0);
		cea.setParallel1(30.0);
		cea.setEccentricity(0.0);
		cea.setRadius(1.0);

		value.lat = 0.6623090;
		value.lon = 2.3428242;

		cea.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(80.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (Forward Ellipsoid)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaEllipsoidForward() {

		cea.setOriginLon(-75.0);
		cea.setParallel1(5.0);
		cea.setEccentricity(cea.EarthEccentricity);
		cea.setRadius(cea.EquatorialRadius);

		value.lat = 10.0;
		value.lon = -78.0;

		cea.toProjection(value, answer);

		assertEquals(554248.447600024, answer.lat, ACCURRACY);
		assertEquals(-332699.83260938, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #10 CylindricalEqualArea (Inverse Ellipsoid)
	// --------------------------------------------------------------

	@Test
	public void testCylindricalEqualAreaEllipsoidInverse() {

		cea.setOriginLon(-75.0);
		cea.setParallel1(5.0);
		cea.setEccentricity(cea.EarthEccentricity);
		cea.setRadius(cea.EquatorialRadius);

		value.lat = 554248.5;
		value.lon = -332699.8;

		cea.toLatLon(value, answer);

		assertEquals(10.0, answer.lat, ACCURRACY);
		assertEquals(-78.0, answer.lon, ACCURRACY);
	}
}
