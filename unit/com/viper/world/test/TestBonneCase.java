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

import com.viper.projections.Bonne;
import com.viper.projections.MapPoint;

public class TestBonneCase extends Assert {

	Bonne bonne = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		bonne = new Bonne();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// #11 Bonne (Compute M)
	// --------------------------------------------------------------

	@Test
	public void testBonneM() {

		double a = bonne.EquatorialRadius;
		double e = bonne.EarthEccentricity;
		double lat = 30.0 * bonne.toRadians;

		double answer = bonne.computeM(lat, e);

		assertEquals(3319933.3 / a, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Compute M)
	// --------------------------------------------------------------

	public void testBonneMa() {

		double l = 30.0 * Math.PI / 180.0;
		double c = 0.00676866;

		double answer = 6378206.4 * ((1.0 - c / 4.0 - 3.0 * c * c / 64.0 - 5.0 * c * c * c / 256.0) * l
				- (3.0 * c / 8.0 + 3.0 * c * c / 32.0 + 45.0 * c * c * c / 1024.0) * Math.sin(2.0 * l)
				+ (15.0 * c * c / 256.0 + 45.0 * c * c * c / 1024.0) * Math.sin(4.0 * l) - (35.0 * c * c * c / 3072.0)
				* Math.sin(6 * l));

		assertEquals(3319933.293306424, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Compute M1)
	// --------------------------------------------------------------

	public void testBonneM1() {

		double a = bonne.EquatorialRadius;
		double e = bonne.EarthEccentricity;
		double lat = 40.0 * bonne.toRadians;

		double answer = bonne.computeM(lat, e);

		assertEquals(4429318.9 / a, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Compute T)
	// --------------------------------------------------------------

	public void testBonneT() {

		double u = 29.8737595 * bonne.toRadians;
		double e = 0.001697916;

		double answer = bonne.computeT(u, e);

		assertEquals(30.0 * bonne.toRadians, answer, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Forward Sphere) Page 287
	// --------------------------------------------------------------

	public void testBonneSphereForward() {

		bonne.setParallel1(40.0);
		bonne.setOriginLon(-75.0);
		bonne.setEccentricity(0.0);
		bonne.setRadius(1.0);

		value.lat = 30.0;
		value.lon = -85.0;

		bonne.toProjection(value, answer);

		assertEquals(-0.1661807, answer.lat, ACCURRACY);
		assertEquals(-0.1508418, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Inverse Sphere) Page 287-288
	// --------------------------------------------------------------

	public void testBonneSphereInverse() {

		bonne.setParallel1(40.0);
		bonne.setOriginLon(-75.0);
		bonne.setEccentricity(0.0);
		bonne.setRadius(1.0);

		value.lat = -0.1661807;
		value.lon = -0.1508418;

		bonne.toLatLon(value, answer);

		assertEquals(30.0, answer.lat, ACCURRACY);
		assertEquals(-85.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Forward Ellipsoid) Page 287
	// --------------------------------------------------------------

	public void testBonneEllipsoidForward() {

		bonne.setParallel1(40.0);
		bonne.setOriginLon(-75.0);
		bonne.setEccentricity(bonne.EarthEccentricity);
		bonne.setRadius(bonne.EquatorialRadius);

		value.lat = 30.0;
		value.lon = -85.0;

		bonne.toProjection(value, answer);

		assertEquals(-1056065.0039364, answer.lat, ACCURRACY);
		assertEquals(-962915.0928348, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #11 Bonne (Inverse Ellipsoid) Page 287-288
	// --------------------------------------------------------------

	public void testBonneEllipsoidInverse() {

		bonne.setParallel1(40.0);
		bonne.setOriginLon(-75.0);
		bonne.setEccentricity(bonne.EarthEccentricity);
		bonne.setRadius(bonne.EquatorialRadius);

		value.lat = -1056065.0;
		value.lon = -962915.1;

		bonne.toLatLon(value, answer);

		assertEquals(30.0, answer.lat, ACCURRACY);
		assertEquals(-85.0, answer.lon, ACCURRACY);
	}
}
