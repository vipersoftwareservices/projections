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

import com.viper.projections.Azimuthal;
import com.viper.projections.DataList;
import com.viper.projections.MapPoint;

public class TestAzimuthalCase extends Assert {

	Azimuthal azimuthal = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;
	double ACCURRACY_DEG = 0.1;

	@Before
	public void setUp() {
		azimuthal = new Azimuthal();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Compute M Test)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalM0() {

		double E = 0.0;
		double lat = 80.0 * azimuthal.toRadians;

		double answer = azimuthal.computeM(lat, E);

		assertEquals(lat, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Compute M Test)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalM80() {

		double E = azimuthal.InternationalEccentricity;
		double a = azimuthal.InternationalRadius;
		double lat = 80.0 * azimuthal.toRadians;

		double answer = azimuthal.computeM(lat, E);

		assertEquals(8885403.071554 / a, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Compute M Test)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalM90() {

		double E = azimuthal.InternationalEccentricity;
		double a = azimuthal.InternationalRadius;
		double lat = 90.0 * azimuthal.toRadians;

		double answer = azimuthal.computeM(lat, E);

		assertEquals(10002288.3 / a, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Compute T Test, page 341)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalT() {

		double E1 = 0.0016863;
		double U = 1.3953965;

		double answer = azimuthal.computeT(U, E1);

		assertEquals(1.3962634, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Subtest).
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalDebug() {

		double E = 0.00001; // Spere
		double a = 3.0; // Unit radius
		double lat = -20.0 * azimuthal.toRadians;
		double lat1 = 40.0 * azimuthal.toRadians;

		double M = azimuthal.computeM(lat, E);
		double MP = azimuthal.computeM(lat1, E);
		double answer = MP - M;

		assertEquals(lat1 - lat, answer, ACCURRACY);
		assertEquals(lat, M, ACCURRACY);
		assertEquals(lat1, MP, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Forward Sphere) Page 337-338
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalSphereForward() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(3.0);

		value.lat = -20.0;
		value.lon = 100.0;

		azimuthal.toProjection(value, answer);

		assertEquals(-5.8311398, answer.lon, ACCURRACY);
		assertEquals(5.5444634, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Inverse Sphere) Page 338
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalSphereInverse() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(3.0);

		value.lat = 5.5444634;
		value.lon = -5.8311398;

		azimuthal.toLatLon(value, answer);

		assertEquals(100.0, answer.lon, ACCURRACY);
		assertEquals(-20.0, answer.lat, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Forward Ellipse) Page 338-341
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalEllipsoidForward() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(90.0);
		azimuthal.setEccentricity(azimuthal.InternationalEccentricity);
		azimuthal.setRadius(azimuthal.InternationalRadius);

		value.lat = 80.0;
		value.lon = 5.0;

		azimuthal.toProjection(value, answer);

		assertEquals(289071.168125119, answer.lat, ACCURRACY);
		assertEquals(1078828.2864462312, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Inverse Ellipse) Page 341-344
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalEllipsoidInverse() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(90.0);
		azimuthal.setEccentricity(azimuthal.InternationalEccentricity);
		azimuthal.setRadius(azimuthal.InternationalRadius);

		value.lat = 289071.168125119;
		value.lon = 1078828.2864462312;

		azimuthal.toLatLon(value, answer);

		assertEquals(79.9999395, answer.lat, ACCURRACY);
		assertEquals(5.0000014, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Forward Ellipse)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalEllipsoidForward1() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(azimuthal.InternationalEccentricity);
		azimuthal.setRadius(azimuthal.InternationalRadius);

		value.lat = -20.0;
		value.lon = 100.0;

		azimuthal.toProjection(value, answer);

		assertEquals(6241437.437371429, answer.lat, ACCURRACY);
		assertEquals(-2271697.446237924, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Inverse Ellipse)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalEllipsoidInverse1() {

		azimuthal.setOriginLon(-100.0);
		azimuthal.setParallel1(40.0);
		azimuthal.setEccentricity(azimuthal.InternationalEccentricity);
		azimuthal.setRadius(azimuthal.InternationalRadius);

		value.lat = 6241437.437371429;
		value.lon = -2271697.446237924;

		azimuthal.toLatLon(value, answer);

		assertEquals(-20.0000000, answer.lat, ACCURRACY);
		assertEquals(100.0000000, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #25 Azimuthal Equidistant (Stress)
	// --------------------------------------------------------------

	@Test
	public void testAzimuthalStress() {

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

		list.add(80.0, 10.0, 1.39829, 0.04281);
		list.add(70.0, 10.0, 1.22481, 0.07741);
		list.add(60.0, 10.0, 1.05068, 0.10534);
		list.add(50.0, 10.0, 0.87609, 0.12765);

		list.add(80.0, 20.0, 1.40434, 0.08469);
		list.add(80.0, 30.0, 1.41435, 0.12469);

		azimuthal.setOriginLon(0.0);
		azimuthal.setParallel1(0.0);
		azimuthal.setEccentricity(0.0);
		azimuthal.setRadius(1.0);

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			azimuthal.toProjection(mp, answer);

			assertEquals("forward lat, mp=" + mp.toString(), pp.lat, answer.lat, ACCURRACY);
			assertEquals("forward lon, mp=" + mp.toString(), pp.lon, answer.lon, ACCURRACY);
		}

		for (int i = 0; i < list.size(); i++) {
			MapPoint mp = list.getLatLon(i);
			MapPoint pp = list.getProjection(i);

			azimuthal.toLatLon(pp, answer);

			assertEquals("inverse lat, pp=" + pp.toString(), mp.lat, answer.lat, ACCURRACY_DEG);
			assertEquals("inverse lon, pp=" + pp.toString(), mp.lon, answer.lon, ACCURRACY_DEG);
		}
	}
}
