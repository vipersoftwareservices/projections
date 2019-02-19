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

import com.viper.projections.EquidistantCylindrical;
import com.viper.projections.MapPoint;

public class TestEquidistantCylindricalCase extends Assert {

	EquidistantCylindrical ec = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		ec = new EquidistantCylindrical();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #11 EquidistantCylindrical (Forward Sphere) Page 287
	// --------------------------------------------------------------
	@Test
	public void testEquidistantCylindricalSphereForward() {

		ec.setOriginLat(0.0);
		ec.setOriginLon(0.0);
		ec.setEccentricity(0.0);
		ec.setRadius(1.0);

		value.lat = 50.0;
		value.lon = -75.0;

		ec.toProjection(value, answer);

		assertEquals(0.8726646259971, answer.lat, ACCURRACY);
		assertEquals(-1.133626463863, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 EquidistantCylindrical (Inverse Sphere) Page 287-288
	// --------------------------------------------------------------

	@Test
	public void testEquidistantCylindricalSphereInverse() {

		ec.setOriginLat(0.0);
		ec.setOriginLon(0.0);
		ec.setEccentricity(0.0);
		ec.setRadius(1.0);

		value.lat = 0.8726646259971;
		value.lon = -1.133626463863;

		ec.toLatLon(value, answer);

		assertEquals(50.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}
}
