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
 * ---------------------------------------------------------------
 */

package com.viper.projections;

public class Azimuthal extends MapProjection {

	public Azimuthal() {
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

	public double computeM(double lat, double E) {

		double E2 = E * E;
		double E4 = E2 * E2;
		double E6 = E4 * E2;

		return (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0) * lat
				- (3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0)
				* Math.sin(2.0 * lat)
				+ (15.0 * E4 / 256.0 + 45.0 * E6 / 1024.0)
				* Math.sin(4.0 * lat) - (35.0 * E6 / 3072.0)
				* Math.sin(6.0 * lat);
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

	public double computeT(double U, double E) {

		double E2 = E * E;
		double E3 = E2 * E;
		double E4 = E3 * E;

		return U + (3.0 * E / 2.0 - 27.0 * E3 / 32.0) * sin(2.0 * U)
				+ (21.0 * E2 / 16.0 - 55.0 * E4 / 32.0) * sin(4.0 * U)
				+ (151.0 * E3 / 96.0) * sin(6.0 * U) + (1097.0 * E4 / 512.0)
				* sin(8.0 * U);
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

		//  o Convert to Radians
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 5-3
		double c = acos(sin(lat1) * sin(lat) + cos(lat1) * cos(lat)
				* cos(lon - lon0));

		//  o Formula 25-2
		double k = c / sin(c);

		//  o Formula 22-4
		pp.lon = a * k * cos(lat) * sin(lon - lon0);

		//  o Formula 21-30, page 197
		pp.lat = a
				* k
				* (cos(lat1) * sin(lat) - sin(lat1) * cos(lat)
						* cos(lon - lon0));
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

		//  o Convert to Radians
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		// o Formula 3-21, page 197
		double M = computeM(lat, E);
		double MP = computeM(lat1, E);

		// o Formula 25-16,
		double p = MP - M;
		double sgn = -1.0;
		if (lat1 < 0.0) {
			p = MP + M;
			sgn = 1.0;
		}

		//  o Formula 21-31, page 197
		pp.lon = a * p * Math.sin(lon - lon0);

		//  o Formula 21-30, page 197
		pp.lat = sgn * a * p * Math.cos(lon - lon0);
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

	public void toLatLonEllipsoid(MapPoint pp, MapPoint mp) {

		double lat = pp.lat;
		double lon = pp.lon;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		double E2 = E * E;
		double E4 = E2 * E2;
		double E6 = E4 * E2;

		// o Formula 3-21, page 197
		double MP = computeM(lat1, E) * a;

		// Formula 20-18
		double P = Math.sqrt(lat * lat + lon * lon);

		// Formula 25-29, page 201
		double M = (lat < 0.0) ? P - MP : MP - P;

		// Formula 3-24, page 201
		double E1 = (1.0 - Math.sqrt(1.0 - E2)) / (1.0 + Math.sqrt(1.0 - E2));

		// Formula 7-19, page 201
		double U = M
				/ (a * (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0));

		// Formula 3-26, page 201
		mp.lat = computeT(U, E1) * toDegrees;

		//  o Formula 20-16, 20-17
		if (lat1 >= 0.0) {
			lat = -lat;
		}

		double Lon = (lon0 + Math.atan(lon / lat)) * toDegrees;
		if (lat < 0.0) {
			Lon = Lon + 180.0;
		}

		mp.lon = ((Lon - 180.0) % 360.0) + 180.0;
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

		double lat = pp.lat;
		double lon = pp.lon;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double sp1 = getParallel1();
		double a = getRadius();

		// Formula 20-18
		double p = sqrt(lat * lat + lon * lon);

		// Formula 25-15
		double c = p / a;

		double Lon = 0.0;
		if (sp1 == 90.0) {
			Lon = (lon0 + atan(lon / -lat));

		} else if (sp1 == -90.0) {
			Lon = (lon0 + atan(lon / lat));

		} else {
			double den = p * cos(lat1) * cos(c) - lat * sin(lat1) * sin(c);
			double num = lon * sin(c);
			Lon = lon0 + atan(num / den);
			if (den < 0.0 && num != 0.0) {
				Lon = Lon + 180.0 * toRadians;
			}
		}

		double Lat = asin(cos(c) * sin(lat1) + lat * sin(c) * cos(lat1) / p);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}
}
