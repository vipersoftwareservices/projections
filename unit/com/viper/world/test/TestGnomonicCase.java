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
import com.viper.projections.Gnomonic;
import com.viper.projections.MapPoint;

public class TestGnomonicCase extends Assert {

	Gnomonic g = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		g = new Gnomonic();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	private double abs(double value) {
		return (value < 0.0) ? -value : value;
	}

	// --------------------------------------------------------------
	// #16 Gnomonic (Forward Sphere)
	// --------------------------------------------------------------
	@Test
	public void testGnomonicSphereForward() {

		g.setParallel1(40.0);
		g.setOriginLon(-100.0);
		g.setEccentricity(0.0);
		g.setRadius(1.0);

		value.lat = 30.0;
		value.lon = -110.0;

		g.toProjection(value, answer);

		assertEquals(-0.1694739, answer.lat, ACCURRACY);
		assertEquals(-0.1542826, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 Gnomonic (Inverse Sphere)
	// --------------------------------------------------------------

	@Test
	public void testGnomonicSphereInverse() {

		g.setParallel1(40.0);
		g.setOriginLon(-100.0);
		g.setEccentricity(0.0);
		g.setRadius(1.0);

		value.lat = -0.1694739;
		value.lon = -0.1542826;

		g.toLatLon(value, answer);

		assertEquals(30.0, answer.lat, ACCURRACY);
		assertEquals(-110.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 Gnomonic (NorthPole)
	// --------------------------------------------------------------

	@Test
	public void testGnomonicNorthPole1() {

		g.setOriginLon(0.0);
		g.setOriginLat(0.0);
		g.setParallel1(0.0);
		g.setEccentricity(0.0);
		g.setRadius(1.0);

		value.lat = 90.0;
		value.lon = 0.0;

		g.toLatLon(value, answer);

		// assertEquals(0.0, answer.lat, ACCURRACY);
		// assertEquals(0.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 Gnomonic (SouthPole)
	// --------------------------------------------------------------

	@Test
	public void testGnomonicSouthPole1() {

		g.setOriginLon(0.0);
		g.setOriginLat(0.0);
		g.setParallel1(0.0);
		g.setEccentricity(0.0);
		g.setRadius(1.0);

		value.lat = -90.0;
		value.lon = 0.0;

		g.toLatLon(value, answer);

		// assertEquals(0.0, answer.lat, ACCURRACY);
		// assertEquals(0.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #16 Stress Gnomonic Spherical - Forward
	// --------------------------------------------------------------

	@Test
	public void testGnomonicSphericalStress1() {

		DataList list = DataList.createInstance();
		list.add(80.0, 0.0, 5.6713, 0.0);
		list.add(70.0, 0.0, 2.7475, 0.0);
		list.add(60.0, 0.0, 1.7321, 0.0);
		list.add(50.0, 0.0, 1.1918, 0.0);
		list.add(40.0, 0.0, 0.8391, 0.0);
		list.add(30.0, 0.0, 0.5774, 0.0);
		list.add(20.0, 0.0, 0.3640, 0.0);
		list.add(10.0, 0.0, 0.1763, 0.0);
		list.add(0.0, 0.0, 0.0000, 0.0);

		list.add(80.0, 10.0, 5.7588, 0.1763);
		list.add(70.0, 10.0, 2.7899, 0.1763);
		list.add(60.0, 10.0, 1.7588, 0.1763);
		list.add(50.0, 10.0, 1.2101, 0.1763);
		list.add(40.0, 10.0, 0.8520, 0.1763);
		list.add(30.0, 10.0, 0.5863, 0.1763);
		list.add(20.0, 10.0, 0.3696, 0.1763);
		list.add(10.0, 10.0, 0.1790, 0.1763);
		list.add(0.0, 10.0, 0.0000, 0.1763);

		list.add(80.0, 20.0, 6.0353, 0.3640);
		list.add(70.0, 20.0, 2.9238, 0.3640);
		list.add(60.0, 20.0, 1.8432, 0.3640);
		list.add(50.0, 20.0, 1.2682, 0.3640);
		list.add(40.0, 20.0, 0.8930, 0.3640);
		list.add(30.0, 20.0, 0.6144, 0.3640);
		list.add(20.0, 20.0, 0.3873, 0.3640);
		list.add(10.0, 20.0, 0.1876, 0.3640);
		list.add(0.0, 20.0, 0.0000, 0.3640);

		list.add(80.0, 30.0, 6.5486, 0.5774);
		list.add(70.0, 30.0, 3.1725, 0.5774);
		list.add(60.0, 30.0, 2.0000, 0.5774);
		list.add(50.0, 30.0, 1.3761, 0.5774);
		list.add(40.0, 30.0, 0.9689, 0.5774);
		list.add(30.0, 30.0, 0.6667, 0.5774);
		list.add(20.0, 30.0, 0.4203, 0.5774);
		list.add(10.0, 30.0, 0.2036, 0.5774);
		list.add(0.0, 30.0, 0.0000, 0.5774);

		list.add(80.0, 40.0, 7.4033, 0.8391);
		list.add(70.0, 40.0, 3.5866, 0.8391);
		list.add(60.0, 40.0, 2.2610, 0.8391);
		list.add(50.0, 40.0, 1.5557, 0.8391);
		list.add(40.0, 40.0, 1.0954, 0.8391);
		list.add(30.0, 40.0, 0.7537, 0.8391);
		list.add(20.0, 40.0, 0.4751, 0.8391);
		list.add(10.0, 40.0, 0.2302, 0.8391);
		list.add(0.0, 40.0, 0.0000, 0.8391);

		list.add(80.0, 50.0, 8.8229, 1.1918);
		list.add(70.0, 50.0, 4.2743, 1.1918);
		list.add(60.0, 50.0, 2.6946, 1.1918);
		list.add(50.0, 50.0, 1.8540, 1.1918);
		list.add(40.0, 50.0, 1.3054, 1.1918);
		list.add(30.0, 50.0, 0.8982, 1.1918);
		list.add(20.0, 50.0, 0.5662, 1.1918);
		list.add(10.0, 50.0, 0.2743, 1.1918);
		list.add(0.0, 50.0, 0.0000, 1.1918);

		list.add(80.0, 60.0, 11.3426, 1.7321);
		list.add(70.0, 60.0, 5.4950, 1.7321);
		list.add(60.0, 60.0, 3.4641, 1.7321);
		list.add(50.0, 60.0, 2.3835, 1.7321);
		list.add(40.0, 60.0, 1.6782, 1.7321);
		list.add(30.0, 60.0, 1.1547, 1.7321);
		list.add(20.0, 60.0, 0.7279, 1.7321);
		list.add(10.0, 60.0, 0.3527, 1.7321);
		list.add(0.0, 60.0, 0.0000, 1.7321);

		list.add(80.0, 70.0, 16.5817, 2.7475);
		list.add(70.0, 70.0, 8.0331, 2.7475);
		list.add(60.0, 70.0, 5.0642, 2.7475);
		list.add(50.0, 70.0, 3.4845, 2.7475);
		list.add(40.0, 70.0, 2.4534, 2.7475);
		list.add(30.0, 70.0, 1.6881, 2.7475);
		list.add(20.0, 70.0, 1.0642, 2.7475);
		list.add(10.0, 70.0, 0.5155, 2.7475);
		list.add(0.0, 70.0, 0.0000, 2.7475);

		list.add(80.0, 80.0, 32.6596, 5.6713);
		list.add(70.0, 80.0, 15.8221, 5.6713);
		list.add(60.0, 80.0, 9.9745, 5.6713);
		list.add(50.0, 80.0, 6.8630, 5.6713);
		list.add(40.0, 80.0, 4.8322, 5.6713);
		list.add(30.0, 80.0, 3.3248, 5.6713);
		list.add(20.0, 80.0, 2.0960, 5.6713);
		list.add(10.0, 80.0, 1.0154, 5.6713);
		list.add(0.0, 80.0, 0.0000, 5.6713);

		g.setOriginLon(0.0);
		g.setOriginLat(0.0);
		g.setParallel1(0.0);
		g.setEccentricity(0.0);
		g.setRadius(1.0);

		// North East quadrant
		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mp.lat = abs(mp.lat);
			mp.lon = abs(mp.lon);

			pp.lat = abs(pp.lat);
			pp.lon = abs(pp.lon);

			g.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		// North West quadrant
		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mp.lat = abs(mp.lat);
			mp.lon = -abs(mp.lon);

			pp.lat = abs(pp.lat);
			pp.lon = -abs(pp.lon);

			g.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
		// South East quadrant
		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mp.lat = -abs(mp.lat);
			mp.lon = abs(mp.lon);

			pp.lat = -abs(pp.lat);
			pp.lon = abs(pp.lon);

			g.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		// South West quadrant
		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			mp.lat = -abs(mp.lat);
			mp.lon = -abs(mp.lon);

			pp.lat = -abs(pp.lat);
			pp.lon = -abs(pp.lon);

			g.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}
	}
}
