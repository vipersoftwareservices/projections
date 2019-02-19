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
