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