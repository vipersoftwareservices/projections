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

public class Albers extends MapProjection {

	public Albers() {
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

	public double computeQ(double Y, double E, double E2) {

		double SY = Math.sin(Y);

		if (E == 0.0) { // Just a guess
			return SY;
		}

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
	 ** Formula 3-12, page 101
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeT(double B, double E) {

		double E2 = E * E;
		double E4 = E2 * E2;
		double E6 = E4 * E2;

		double m3 = 761.0 * E6 / 45360.0;

		return B + (E2 / 3.0 + 31.0 * E4 / 180.0 + 517.0 * E6 / 5040.0)
				* Math.sin(2.0 * B) + (23.0 * E4 / 360.0 + 251.0 * E6 / 3780.0)
				* Math.sin(4.0 * B) + (761.0 * E6 / 45360.0)
				* Math.sin(6.0 * B);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** LN(ER) = -0.164916562
	 ** @exception
	 **
	 ** Formula 3-12, page 101
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeB(double Q, double E) {

		double ER = (1.0 - E) / (1.0 + E);

		double D = 1.0 - ((1.0 - E * E) / (2.0 * E)) * Math.log(ER);

		return Math.asin(Q / D);
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

		//  o Convert Degrees to Radians
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;

		double a = getRadius();

		// Formula 14-6
		double N = (sin(lat1) + sin(lat2)) / 2.0;

		// Formula 14-5
		double C = pow(cos(lat1), 2.0) + 2.0 * N * sin(lat1);

		// Formula 14-3a
		double P0 = a * sqrt(C - 2.0 * N * sin(lat0)) / N;

		// Formula 14-4
		double theta = N * (lon - lon0);

		// Formula 14-3
		double P = a * sqrt(C - 2.0 * N * sin(lat)) / N;

		// Formula 14-1
		pp.lon = P * sin(theta);

		// Formula 14-2
		pp.lat = P0 - P * cos(theta);

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

		//  o Convert Degrees to Radians
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;

		double a = getRadius();

		double E = getEccentricity();
		double E2 = E * E;

		//  o Formula 3-12, page 101
		double Q = computeQ(lat, E, E2);

		//  o Formula 3-12, page 101
		double Q0 = computeQ(lat0, E, E2);

		//  o Formula 3-12, page 101
		double Q1 = computeQ(lat1, E, E2);

		//  o Formula 3-12, page 101
		double Q2 = computeQ(lat2, E, E2);

		//  o Formula 14-15
		double slat1 = sin(lat1);
		double M1 = cos(lat1) / sqrt(1.0 - E2 * slat1 * slat1);

		//  o Formula 14-15
		double slat2 = sin(lat2);
		double M2 = cos(lat2) / sqrt(1.0 - E2 * slat2 * slat2);

		//  o Formula 14-14
		double N = (M1 * M1 - M2 * M2) / (Q2 - Q1);

		//  o Formula 14-13, page 101
		double C = M1 * M1 + N * Q1;

		//  o Formula 14-12a, page 101
		double P0 = a * sqrt(C - N * Q0) / N;

		//  o Formula 14-4;
		double T = N * (lon - lon0);

		//  o Formula 14-12,
		double P = a * sqrt(C - N * Q) / N;

		//  o Formula 14-1
		pp.lon = P * sin(T);

		//  o Formula 14-2
		pp.lat = P0 - P * cos(T);
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

		//  o Convert Degrees to Radians
		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;

		double a = getRadius();

		double E = getEccentricity();
		double E2 = E * E;

		//  o Formula 3-12, page 101
		double Q0 = computeQ(lat0, E, E2);

		//  o Formula 3-12, page 101
		double Q1 = computeQ(lat1, E, E2);

		//  o Formula 3-12, page 101
		double Q2 = computeQ(lat2, E, E2);

		//  o Formula 14-15
		double slat1 = sin(lat1);
		double M1 = Math.cos(lat1) / Math.sqrt(1.0 - E2 * slat1 * slat1);

		//  o Formula 14-15
		double slat2 = Math.sin(lat2);
		double M2 = Math.cos(lat2) / Math.sqrt(1.0 - E2 * slat2 * slat2);

		//  o Formula 14-14
		double N = (M1 * M1 - M2 * M2) / (Q2 - Q1);

		//  o Formula 14-13, page 101
		double C = M1 * M1 + N * Q1;

		//  o Formula 14-12a, page 101
		double P0 = a * Math.sqrt(C - N * Q0) / N;

		//  o Formula 14-11, page 102
		double T = Math.atan(lon / (P0 - lat));

		//  o Formula 14-10, page 102
		double P = Math.sqrt(lon * lon + pow(P0 - lat, 2.0));

		//  o Formula 14-19, page 102
		double Q = (C - P * P * N * N / (a * a)) / N;

		//  o Formula 14-21
		double B = computeB(Q, E);

		//  o Formula 3-18
		double Lat = computeT(B, E);

		//  o Formula 14-9
		double Lon = lon0 + (T / N);

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

	public void toLatLonSphere(MapPoint pp, MapPoint mp) {

		//  o Convert Degrees to Radians
		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		//  o Formula 14-5
		double N = (Math.sin(lat1) + Math.sin(lat2)) / 2.0;

		//  o Formula 14-5, page 100
		double C = Math.pow(Math.cos(lat1), 2.0) + 2.0 * N * Math.sin(lat1);

		//  o Formula 14-3a, page 100
		double P0 = a * Math.sqrt(C - 2.0 * N * Math.sin(lat0)) / N;

		//  o Formula 14-11, page 102
		double T = Math.atan(lon / (P0 - lat));

		//  o Formula 14-10, page 102
		double P = Math.sqrt(lon * lon + Math.pow(P0 - lat, 2.0));

		double Lat = Math.asin((C - Math.pow(P * N / a, 2.0)) / (2.0 * N));

		//  o Formula 14-9, page 102
		double Lon = lon0 + (T / N);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}
}
