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

public class Cassini extends MapProjection {

	public Cassini() {
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

		//  o Convert Degrees to radians.
		double lon = (mp.lon - getOriginLon()) * toRadians;
		double lat = mp.lat * toRadians;
		double lat0 = getOriginLat() * toRadians;

		double a = getRadius();

		double E = getEccentricity();
		double E2 = Math.pow(E, 2.0);
		double E4 = Math.pow(E, 4.0);
		double E6 = Math.pow(E, 6.0);

		//  o Formula 4-20, page 95.
		double N = a / Math.sqrt(1.0 - E2 * Math.pow(Math.sin(lat), 2.0));

		//  o Formula 8-13, page 95.
		double T = Math.pow(Math.tan(lat), 2.0);

		//  o Formula 8-15, page 95.
		double A = lon * Math.cos(lat);
		double A2 = Math.pow(A, 2.0);
		double A3 = Math.pow(A, 3.0);
		double A4 = Math.pow(A, 4.0);
		double A5 = Math.pow(A, 5.0);

		//  o Formula 8-14, page 95.
		double C = E2 * Math.pow(Math.cos(lat), 2.0) / (1.0 - E2);

		// Formula 3-21, page 61
		double M1 = 1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0;
		double M2 = 3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0;
		double M3 = 15.0 * E4 / 256.0 + 45 * E6 / 1024.0;
		double M4 = 35.0 * E6 / 3072.0;

		double M = a
				* (M1 * lat - M2 * Math.sin(2.0 * lat) + M3
						* Math.sin(4.0 * lat) - M4 * Math.sin(6.0 * lat));

		double M0 = a
				* (M1 * lat0 - M2 * Math.sin(2.0 * lat0) + M3
						* Math.sin(4.0 * lat0) - M4 * Math.sin(6.0 * lat0));

		//  o Formula 13-7, page 95.
		pp.lon = N * (A - T * A3 / 6.0 - (8.0 - T + 8.0 * C) * T * A5 / 120);

		//   o Formula 13-8, page 95.
		pp.lat = M - M0 + N * Math.tan(lat)
				* (A2 / 2.0 + (5.0 - T + 6.0 * C) * A4 / 24.0);
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

		double a = getRadius();

		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;

		double E = getEccentricity();
		double E2 = Math.pow(E, 2.0);
		double E3 = Math.pow(E, 3.0);
		double E4 = Math.pow(E, 4.0);
		double E5 = Math.pow(E, 5.0);
		double E6 = Math.pow(E, 6.0);

		//  o Formula 3-24, page 95.
		double S = Math.sqrt(1.0 - E2);
		double E1 = (1.0 - S) / (1.0 + S);
		double E1_2 = Math.pow(E1, 2.0);
		double E1_3 = Math.pow(E1, 3.0);
		double E1_4 = Math.pow(E1, 4.0);

		// Formula 3-21, page 61
		double m1 = 1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0;
		double m2 = 3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0;
		double m3 = 15.0 * E4 / 256.0 + 45 * E6 / 1024.0;
		double m4 = 35.0 * E6 / 3072.0;

		double M0 = a
				* (m1 * lat0 - m2 * Math.sin(2.0 * lat0) + m3
						* Math.sin(4.0 * lat0) - m4 * Math.sin(6.0 * lat0));

		//  o Formula 13-12, page 95.
		double M1 = M0 + lat;

		//  o Formula 7-19, page 95.
		double U1 = M1
				/ (a * (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0));

		//  o Formula 3-26, page 95.
		double lat1 = U1 + (3.0 * E1 / 2.0 - 27 * E3 / 32.0)
				* Math.sin(2.0 * U1)
				+ (21.0 * E1_2 / 16.0 - 55.0 * E1_4 / 32.0)
				* Math.sin(4.0 * U1) + (151.0 * E1_3 / 96.0)
				* Math.sin(6.0 * U1) + (1097.0 * E1_4 / 512.0)
				* Math.sin(8.0 * U1);

		//  o Formula 8-22, page 95.
		double T1 = Math.pow(Math.tan(lat1), 2.0);

		//  o Formula 8-23, page 95.
		double ES = E2 * Math.pow(Math.sin(lat1), 2.0);
		double N1 = a / Math.sqrt(1.0 - ES);

		//  o Formula 8-24, page 95.
		double R1 = a * (1.0 - E2) / Math.pow(1.0 - ES, 1.5);

		//  o Formula 13-13, page 95.
		double D = lon / N1;
		double D2 = Math.pow(D, 2.0);
		double D3 = Math.pow(D, 3.0);
		double D4 = Math.pow(D, 4.0);
		double D5 = Math.pow(D, 5.0);

		//  o Formula 13-10, page 95.
		double Lat = lat1 - (N1 * Math.tan(lat1 / R1))
				* (D2 / 2.0 - (1.0 + 3.0 * T1) * D4 / 24.0);

		//  o Formula 13-11, page 95.
		double Lon = lon0
				+ (D - T1 * D3 / 3.0 + (1.0 + 3.0 * T1) * T1 * D5 / 15.0)
				/ Math.cos(lat1);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}
}
