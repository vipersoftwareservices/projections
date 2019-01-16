/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * 36710 Nichols Ave, Fremont CA, 94536
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
