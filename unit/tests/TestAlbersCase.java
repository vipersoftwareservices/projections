/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2003 
 *
 * @note 
 *        
 * ---------------------------------------------------------------
 */

package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.viper.projections.Albers;
import com.viper.projections.MapPoint;

public class TestAlbersCase extends Assert {

	Albers albers = null;
	MapPoint answer = null;
	MapPoint value = null;
	MapPoint expected = null;

	double ACCURRACY = 0.001;

	@Before
	public void setUp() {
		albers = new Albers();
		answer = new MapPoint();
		value = new MapPoint();
		expected = new MapPoint();
	}

	// --------------------------------------------------------------
	// Albers (Compute Q)
	// --------------------------------------------------------------

	@Test
	public void testAlbersComputeQ() {

		double Q, E, E2, Y;

		E = 0.0822719;
		E2 = Math.pow(E, 2.0);
		Y = 29.50 * albers.toRadians;

		Q = albers.computeQ(Y, E, E2);
		assertEquals("Eccentricity==0.0822719)", 0.9792529, Q, ACCURRACY);

		E = 0.0822719;
		E2 = Math.pow(E, 2.0);
		Y = 45.50 * albers.toRadians;

		Q = albers.computeQ(Y, E, E2);
		assertEquals("Eccentricity==0.0822719)", 1.4201080, Q, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #14 Albers (Forward Sphere) Page 291
	// --------------------------------------------------------------

	@Test
	public void testAlbersSphereForward() {

		albers.setOriginLat(23.0);
		albers.setOriginLon(-96.0);
		albers.setParallel1(29.50);
		albers.setParallel2(45.50);
		albers.setEccentricity(0.0);
		albers.setRadius(1.0);

		value.lat = 35.0;
		value.lon = -75.0;

		albers.toProjection(value, answer);

		assertEquals(0.2416774, answer.lat, ACCURRACY);
		assertEquals(0.2952720, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #14 Albers (Inverse Sphere) Page 291-292
	// --------------------------------------------------------------

	@Test
	public void testAlbersSphereInverse() {

		albers.setOriginLat(23.0);
		albers.setOriginLon(-96.0);
		albers.setParallel1(29.50);
		albers.setParallel2(45.50);
		albers.setEccentricity(0.0);
		albers.setRadius(1.0);

		value.lat = 0.2416774;
		value.lon = 0.2952720;

		albers.toLatLon(value, answer);

		assertEquals(35.0, answer.lat, ACCURRACY);
		assertEquals(-75.0, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #14 Albers (Forward Ellipsoid) Page 292
	// --------------------------------------------------------------

	@Test
	public void testAlbersEllipsoidForward() {

		albers.setOriginLat(23.0);
		albers.setOriginLon(-96.0);
		albers.setParallel1(29.50);
		albers.setParallel2(45.50);
		albers.setEccentricity(albers.EarthEccentricity);
		albers.setRadius(albers.EquatorialRadius);

		value.lat = 35.0;
		value.lon = -75.0;

		albers.toProjection(value, answer);

		assertEquals(1535924.9987982698, answer.lat, ACCURRACY);
		assertEquals(1885472.72822904, answer.lon, ACCURRACY);
	}

	// --------------------------------------------------------------
	// #14 Albers (Inverse Ellipsoid) Page 291-292
	// --------------------------------------------------------------

	@Test
	public void testAlbersEllipsoidInverse() {

		albers.setOriginLat(23.0);
		albers.setOriginLon(-96.0);
		albers.setParallel1(29.50);
		albers.setParallel2(45.50);
		albers.setEccentricity(albers.EarthEccentricity);
		albers.setRadius(albers.EquatorialRadius);

		value.lat = 1535925.0;
		value.lon = 1885472.7;

		albers.toLatLon(value, answer);

		assertEquals(34.9997335, answer.lat, ACCURRACY);
		assertEquals(-75.0000015, answer.lon, ACCURRACY);
	}
}
