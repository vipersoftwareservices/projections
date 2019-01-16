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

public class Bonne extends MapProjection {

	public Bonne() {
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
				* sin(2.0 * lat) + (15.0 * E4 / 256.0 + 45.0 * E6 / 1024.0)
				* sin(4.0 * lat) - (35.0 * E6 / 3072.0) * sin(6.0 * lat);
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

	public double computeT(double u, double E) {

		double E2 = E * E;
		double E3 = E * E2;
		double E4 = E * E3;

		return u + (3.0 * E / 2.0 - 27.0 * E3 / 32.0) * Math.sin(2.0 * u)
				+ (21.0 * E2 / 16.0 - 55.0 * E4 / 32.0) * Math.sin(4.0 * u)
				+ (151.0 * E3 / 96.0) * Math.sin(6.0 * u)
				+ (1097.0 * E4 / 512.0) * Math.sin(8.0 * u);
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

		//  o Grab all parameters
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();
		double e = getEccentricity();

		// Formula 14-15, page 140
		double m = cos(lat) / sqrt(1.0 - pow(e * sin(lat), 2.0));
		double m1 = cos(lat1) / sqrt(1.0 - pow(e * sin(lat1), 2.0));

		// Formula 3-21, page 140
		double M = computeM(lat, e);
		double M1 = computeM(lat1, e);

		//  o Formula 19-8
		double p = m1 / sin(lat1) + M1 - M;

		//  o Formula 19-9
		double E = m * (lon - lon0) / p;

		//  o Formula 19-3.
		pp.lon = a * p * sin(E);

		//  o Formula 19-4.
		pp.lat = a * (m1 / sin(lat1) - p * cos(E));
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

		//  o Grab all parameters
		double lat = pp.lat;
		double lon = pp.lon;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();
		double e = getEccentricity();
		double e2 = pow(e, 2.0);
		double e4 = pow(e, 4.0);
		double e6 = pow(e, 6.0);

		// Formula 14-15, page 140
		double m1 = cos(lat1) / sqrt(1.0 - pow(e * sin(lat1), 2.0));

		// Formula 3-21, page 140
		double M1 = computeM(lat1, e) * a;

		// Formula 19-12,
		double p = sign(lat1)
				* sqrt(lon * lon + square(a * m1 / sin(lat1) - lat));

		// Formula 19-13
		double M = a * m1 / sin(lat1) + M1 - p;

		// Formula 7-19
		double u = M
				/ (a * (1.0 - e2 / 4.0 - 3.0 * e4 / 64.0 - 5.0 * e6 / 256.0));

		//  o Formula 3-24
		double et = sqrt(1.0 - e * e);
		double e1 = (1.0 - et) / (1.0 + et);

		//  o Formula 3-26 
		double Lat = computeT(u, e1);

		//  o Formula 14-15
		double m = cos(Lat) / sqrt(1.0 - pow(e * sin(Lat), 2.0));

		//   o Formula 19-14
		double Lon = lon0 + p * atan(lon / (a * m1 / sin(lat1) - lat))
				/ (a * m);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}
}
