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


package com.viper.world.test;

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
