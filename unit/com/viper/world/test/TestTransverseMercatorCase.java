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
import com.viper.projections.TransverseMercator;

public class TestTransverseMercatorCase extends Assert {

	TransverseMercator tm = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		tm = new TransverseMercator();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #8 Transverse Mercator (Forward Ellipse) Page 269-270
	// --------------------------------------------------------------
	@Test
	public void testTransverseMercatorSphereForward() {

		tm.setOriginLat(0.0);
		tm.setOriginLon(-75.0);
		tm.setEccentricity(tm.EarthEccentricity);
		tm.setRadius(tm.EquatorialRadius);

		value.lat = 40.5;
		value.lon = -73.5;

		tm.toProjection(value, answer);

		assertEquals(4484124.408238, answer.lat, ACCURRACY);
		assertEquals(127106.4676, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #8 Transverse Mercator (Inverse Ellipse) Page 270-271
	// --------------------------------------------------------------
	@Test
	public void testTransverseMercatorSphereInverse() {

		tm.setOriginLat(0.0);
		tm.setOriginLon(-75.0);
		tm.setEccentricity(tm.EarthEccentricity);
		tm.setRadius(tm.EquatorialRadius);

		value.lat = 4484124.408238;
		value.lon = 127106.4676;

		tm.toLatLon(value, answer);

		assertEquals(40.501679, answer.lat, ACCURRACY);
		assertEquals(-73.469622, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	//
	//
	// --------------------------------------------------------------
	@Test
	public void testTransverseMercatorSphericalStress() {

		DataList list = DataList.createInstance();
		list.add(90.0, 0.0, 1.57080, 0.0);
		list.add(80.0, 0.0, 1.39626, 0.0);
		list.add(70.0, 0.0, 1.22173, 0.0);
		list.add(60.0, 0.0, 1.04720, 0.0);
		list.add(50.0, 0.0, 0.87266, 0.0);
		list.add(40.0, 0.0, 0.69813, 0.0);
		list.add(30.0, 0.0, 0.52360, 0.0);
		list.add(20.0, 0.0, 0.34907, 0.0);
		list.add(10.0, 0.0, 0.17453, 0.0);
		list.add(0.0, 0.0, 0.00000, 0.0);

		list.add(90.0, 10.0, 1.57080, 0.0);
		list.add(80.0, 10.0, 1.39886, 0.03016);
		list.add(70.0, 10.0, 1.22662, 0.05946);
		list.add(60.0, 10.0, 1.05380, 0.08704);
		list.add(50.0, 10.0, 0.88019, 0.11209);
		list.add(40.0, 10.0, 0.70568, 0.13382);
		list.add(30.0, 10.0, 0.53025, 0.15153);
		list.add(20.0, 10.0, 0.35401, 0.16465);
		list.add(10.0, 10.0, 0.17717, 0.17271);
		list.add(0.0, 10.0, 0.00000, 0.17543);

		list.add(90.0, 20.0, 1.57080, 0.0);
		list.add(90.0, 30.0, 1.57080, 0.0);
		list.add(90.0, 40.0, 1.57080, 0.0);
		list.add(90.0, 50.0, 1.57080, 0.0);
		list.add(90.0, 60.0, 1.57080, 0.0);
		list.add(90.0, 70.0, 1.57080, 0.0);
		list.add(90.0, 80.0, 1.57080, 0.0);
		list.add(90.0, 90.0, 1.57080, 0.0);

		tm.setOriginLat(0.0);
		tm.setOriginLon(0.0);
		tm.setEccentricity(0.0);
		tm.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			tm.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
	}
}