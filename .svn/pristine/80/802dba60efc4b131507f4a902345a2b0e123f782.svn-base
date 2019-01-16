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

package com.viper.projections;

public class CylindricalEqualArea extends MapProjection {

	public CylindricalEqualArea() {
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeT(double B, double e) {

		double e2 = e * e;
		double e4 = e2 * e2;
		double e6 = e4 * e2;

		return B + (e2 / 3.0 + 31.0 * e4 / 180.0 + 517.0 * e6 / 5040.0)
				* sin(2.0 * B) + (23.0 * e4 / 360.0 + 251.0 * e6 / 3780.0)
				* sin(4.0 * B) + (761.0 * e6 / 45360.0) * sin(6.0 * B);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Formula 3-12, page 101
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeQ(double Y, double E) {

		double SY = Math.sin(Y);

		if (E == 0.0) { // Just a guess
			return SY;
		}

		double E2 = E * E;

		return (1.0 - E2)
				* (SY / (1.0 - E2 * SY * SY) - (1.0 / (2.0 * E))
						* Math.log((1.0 - E * SY) / (1.0 + E * SY)));
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Formula 10-13
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeK0(double lat, double E) {

		double slat = sin(lat);

		return cos(lat) / sqrt(1.0 - E * E * slat * slat);

	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toProjection(MapPoint mp, MapPoint pp) {

		if (getEccentricity() == 0.0) {
			toProjectionSphere(mp, pp);
		} else {
			toProjectionEllipsoid(mp, pp);
		}
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toProjectionSphere(MapPoint mp, MapPoint pp) {

		//  o Convert Degrees to radians.
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double a = getRadius();

		//  o Formula 10-1.
		pp.lon = a * (lon - lon0) * cos(lat1);

		//  o Formula 10-2.
		pp.lat = a * sin(lat) / cos(lat1);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toProjectionEllipsoid(MapPoint mp, MapPoint pp) {

		//  o Convert Degrees to radians.
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		// Formula 10-13
		double k0 = computeK0(lat1, E);

		// Formula 3-12
		double Q = computeQ(lat1, E);

		//  o Formula 10-1.
		pp.lon = a * k0 * (lon - lon0);

		//  o Formula 10-2.
		pp.lat = a * Q / (2.0 * k0);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toLatLon(MapPoint pp, MapPoint mp) {
		if (getEccentricity() == 0.0) {
			toLatLonSphere(pp, mp);
		} else {
			toLatLonEllipsoid(pp, mp);
		}
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toLatLonSphere(MapPoint pp, MapPoint mp) {

		double lon = pp.lon;
		double lat = pp.lat;
		double lat1 = getParallel1() * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double a = getRadius();

		// o Formula 10-6
		double Lat = asin((lat / a) * cos(lat1));

		// o Formula 10-7
		double Lon = (lon / (a * cos(lat1))) + lon0;

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toLatLonEllipsoid(MapPoint pp, MapPoint mp) {

		double lon = pp.lon;
		double lat = pp.lat;
		double lat1 = getParallel1() * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		// Formula 10-13
		double k0 = computeK0(lat1, E);

		// Formula 3-12
		double qp = computeQ(lat1, E);

		// Formula 10-26
		double B = asin(2.0 * lat * k0 / (a * qp));

		// Formula 10-27
		double Lon = lon0 + lon / (a * k0);

		// Formula 3-18
		double Lat = computeT(B, E);

		mp.lon = Lon * toDegrees;
		mp.lat = Lat * toDegrees;
	}
}
