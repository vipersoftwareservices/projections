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
