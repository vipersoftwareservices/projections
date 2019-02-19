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

import com.viper.projections.DataList;
import com.viper.projections.MapPoint;
import com.viper.projections.Miller;

public class TestMillerCase extends Assert {

	Miller miller = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		miller = new Miller();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #11 Miller (Forward Sphere) Page 287
	// --------------------------------------------------------------
	@Test
	public void testMillerSphereForward() {

		miller.setOriginLon(0.0);
		miller.setEccentricity(0.0);
		miller.setRadius(1.0);

		value.lat = 50.0;
		value.lon = -75.0;

		miller.toProjection(value, answer);

		assertEquals(0.9536371, answer.lat, ACCURRACY);
		assertEquals(-1.3089969, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Miller (Inverse Sphere) Page 287-288
	// --------------------------------------------------------------
	@Test
	public void testMillerSphereInverse() {

		miller.setOriginLon(0.0);
		miller.setEccentricity(0.0);
		miller.setRadius(1.0);

		value.lat = 0.9536371;
		value.lon = -1.3089969;

		miller.toLatLon(value, answer);

		assertEquals(50.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	//
	//
	// --------------------------------------------------------------
	@Test
	public void testMillerSphericalStress() {

		DataList list = DataList.createInstance();
		list.add(90.0, 0.0, 2.30341, 0.0);
		list.add(85.0, 0.0, 2.04742, 0.0);
		list.add(80.0, 0.0, 1.83239, 0.0);
		list.add(75.0, 0.0, 1.64620, 0.0);
		list.add(70.0, 0.0, 1.48131, 0.0);
		list.add(65.0, 0.0, 1.33270, 0.0);
		list.add(60.0, 0.0, 1.19683, 0.0);
		list.add(55.0, 0.0, 1.07113, 0.0);
		list.add(50.0, 0.0, 0.95364, 0.0);
		list.add(45.0, 0.0, 0.84284, 0.0);
		list.add(40.0, 0.0, 0.73754, 0.0);
		list.add(35.0, 0.0, 0.63674, 0.0);
		list.add(30.0, 0.0, 0.53962, 0.0);
		list.add(25.0, 0.0, 0.44547, 0.0);
		list.add(20.0, 0.0, 0.35369, 0.0);
		list.add(15.0, 0.0, 0.26373, 0.0);
		list.add(10.0, 0.0, 0.17510, 0.0);
		list.add(5.0, 0.0, 0.08734, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);

		miller.setOriginLat(0.0);
		miller.setOriginLon(0.0);
		miller.setEccentricity(0.0);
		miller.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			miller.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
	}
}
