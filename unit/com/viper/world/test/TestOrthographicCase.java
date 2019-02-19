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
import com.viper.projections.Orthographic;

public class TestOrthographicCase extends Assert {

	Orthographic orthographic = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;
	double ACCURRACY_DEG = 0.1;

	@Before
	public void setUp() {
		orthographic = new Orthographic();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #25 Orthographic (Forward Sphere) Page 337-338
	// --------------------------------------------------------------
	@Test
	public void testOrthographicSphereForward() {

		orthographic.setOriginLon(-100.0);
		orthographic.setParallel1(40.0);
		orthographic.setEccentricity(0.0);
		orthographic.setRadius(1.0);

		value.lat = 30.0;
		value.lon = -110.0;

		orthographic.toProjection(value, answer);

		assertEquals(-0.1503837, answer.lon, ACCURRACY);
		assertEquals(-0.1651911, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Orthographic (Inverse Sphere) Page 338
	// --------------------------------------------------------------
	@Test
	public void testOrthographicSphereInverse() {

		orthographic.setOriginLon(-100.0);
		orthographic.setParallel1(40.0);
		orthographic.setEccentricity(0.0);
		orthographic.setRadius(1.0);

		value.lat = -0.1651911;
		value.lon = -0.1503837;

		orthographic.toLatLon(value, answer);

		assertEquals(-110.0, answer.lon, ACCURRACY);
		assertEquals(30.0, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Orthographic (Stress)
	// --------------------------------------------------------------
	@Test
	public void testOrthographicStress() {

		DataList list = DataList.createInstance();

		list.add(90.0, 0.0, 1.0000, 0.0);
		list.add(80.0, 0.0, 0.9848, 0.0);
		list.add(70.0, 0.0, 0.9397, 0.0);
		list.add(60.0, 0.0, 0.8660, 0.0);
		list.add(50.0, 0.0, 0.7660, 0.0);
		list.add(40.0, 0.0, 0.6428, 0.0);
		list.add(30.0, 0.0, 0.5000, 0.0);
		list.add(20.0, 0.0, 0.3420, 0.0);
		list.add(10.0, 0.0, 0.1736, 0.0);
		list.add(0.0, 0.0, 0.0000, 0.0);

		list.add(90.0, 10.0, 1.0000, 0.0000);
		list.add(80.0, 10.0, 0.9848, 0.0302);
		list.add(70.0, 10.0, 0.9397, 0.0594);
		list.add(60.0, 10.0, 0.8660, 0.0868);
		list.add(50.0, 10.0, 0.7660, 0.1116);
		list.add(40.0, 10.0, 0.6428, 0.1330);
		list.add(30.0, 10.0, 0.5000, 0.1504);
		list.add(20.0, 10.0, 0.3420, 0.1632);
		list.add(10.0, 10.0, 0.1736, 0.1710);
		list.add(0.0, 10.0, 0.0000, 0.1736);

		list.add(90.0, 20.0, 1.0000, 0.0000);
		list.add(80.0, 20.0, 0.9848, 0.0594);
		list.add(70.0, 20.0, 0.9397, 0.1170);
		list.add(60.0, 20.0, 0.8660, 0.1710);
		list.add(50.0, 20.0, 0.7660, 0.2198);
		list.add(40.0, 20.0, 0.6428, 0.2620);
		list.add(30.0, 20.0, 0.5000, 0.2962);
		list.add(20.0, 20.0, 0.3420, 0.3214);
		list.add(10.0, 20.0, 0.1736, 0.3368);
		list.add(0.0, 20.0, 0.0000, 0.3420);

		list.add(90.0, 30.0, 1.0000, 0.0000);
		list.add(80.0, 30.0, 0.9848, 0.0868);
		list.add(70.0, 30.0, 0.9397, 0.1710);
		list.add(60.0, 30.0, 0.8660, 0.2500);
		list.add(50.0, 30.0, 0.7660, 0.3214);
		list.add(40.0, 30.0, 0.6428, 0.3830);
		list.add(30.0, 30.0, 0.5000, 0.4330);
		list.add(20.0, 30.0, 0.3420, 0.4698);
		list.add(10.0, 30.0, 0.1736, 0.4924);
		list.add(0.0, 30.0, 0.0000, 0.5000);

		orthographic.setOriginLon(0.0);
		orthographic.setParallel1(0.0);
		orthographic.setEccentricity(0.0);
		orthographic.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			orthographic.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
	}
}
