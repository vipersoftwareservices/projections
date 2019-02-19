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

import com.viper.projections.MapPoint;
import com.viper.projections.VerticalPerspective;

public class TestVerticalPerspectiveCase extends Assert {

	VerticalPerspective ec = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		ec = new VerticalPerspective();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #16 VerticalPerspective (Forward Ellipsoid)
	// --------------------------------------------------------------
	@Test
	public void testVerticalPerspectiveEllipsoidForward() {

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
	// #16 VerticalPerspective (Inverse Ellipsoid)
	// --------------------------------------------------------------
	@Test
	public void testVerticalPerspectiveEllipsoidInverse() {

		ec.setOriginLat(23.0);
		ec.setOriginLon(-96.0);
		ec.setParallel1(29.50);
		ec.setParallel2(45.50);
		ec.setEccentricity(ec.EarthEccentricity);
		ec.setRadius(ec.EquatorialRadius);

		value.lat = 1540507.6;
		value.lon = 1885051.9;

		ec.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}
}