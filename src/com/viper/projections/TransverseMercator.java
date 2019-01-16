/*
 * --------------------------------------------------------------
 * VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * 36710 Nichols Ave, Fremont CA, 94536
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information"). You
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

public class TransverseMercator extends MapProjection {

	final double K0 = 0.9996;

	public TransverseMercator() {
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

		// o Convert Degree Values to Radians
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lon0 = getOriginLon() * toRadians;

		// Calculations page 61.
		double a = getRadius();
		double E = getEccentricity();
		double E2 = pow(E, 2.0);
		double E4 = pow(E, 4.0);
		double E6 = pow(E, 6.0);

		// Formula 8-12, page 61.
		double EP2 = E2 / (1.0 - E2);

		// Formula 4-20, page 61.
		double SIN_Y = sin(lat);
		double N = a / sqrt(1.0 - E2 * SIN_Y * SIN_Y);

		// Formula 8-13, page 61.
		double TAN_Y = tan(lat);
		double T = TAN_Y * TAN_Y;

		// Formula 8-14, page 61.
		double COS_Y = cos(lat);
		double C = EP2 * COS_Y * COS_Y;

		// Formula 8-15, page 61.
		double A = (lon - lon0) * COS_Y;

		// Formula 3-21, page 61
		double M1 = 1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0;
		double M2 = 3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0;
		double M3 = 15.0 * E4 / 256.0 + 45 * E6 / 1024.0;
		double M4 = 35.0 * E6 / 3072.0;

		double M = a
				* (M1 * lat - M2 * sin(2.0 * lat) + M3 * sin(4.0 * lat) - M4
						* sin(6.0 * lat));

		double M0 = a
				* (M1 * lat0 - M2 * sin(2.0 * lat0) + M3 * sin(4.0 * lat0) - M4
						* sin(6.0 * lat0));

		double A2 = pow(A, 2.0);
		double A3 = pow(A, 3.0);
		double A4 = pow(A, 4.0);
		double A5 = pow(A, 5.0);
		double A6 = pow(A, 6.0);

		// Formula 8-9, page 61.
		pp.lon = K0
				* N
				* (A + (1.0 - T + C) * A3 / 6.0 + (5.0 - 18.0 * T + T * T
						+ 72.0 * C - 58.0 * EP2)
						* A5 / 120.0);

		// Formula 8-10, page 61.
		pp.lat = K0
				* (M - M0 + N
						* TAN_Y
						* (A2 / 2.0 + (5.0 - T + 9.0 * C + 4.0 * C * C) * A4
								/ 24.0 + (61.0 - 58.0 * T + T * T + 600.0 * C - 330.0 * EP2)
								* A6 / 720.0));
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

		// o Origin
		double lat0 = getOriginLat() * toRadians;

		double a = getRadius();

		// o Formula 8-12, page 64
		double E = getEccentricity();
		double E2 = pow(E, 2.0);
		double E4 = pow(E, 4.0);
		double E6 = pow(E, 6.0);
		double EP2 = E2 / (1.0 - E2);

		// o Formula 3-24, page 63
		double S = sqrt(1.0 - E2);
		double E1 = (1.0 - S) / (1.0 + S);

		// Formula 3-21, page 61
		double M1 = 1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0;
		double M2 = 3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0;
		double M3 = 15.0 * E4 / 256.0 + 45 * E6 / 1024.0;
		double M4 = 35.0 * E6 / 3072.0;

		double M0 = a
				* (M1 * lat0 - M2 * sin(2.0 * lat0) + M3 * sin(4.0 * lat0) - M4
						* sin(6.0 * lat0));

		// o Formula 8-20, page 63.
		double M = M0 + pp.lat / K0;

		// o Formula 7-19, page 63.
		double U = M
				/ (a * (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0));

		// o Formula 3-26, page 53
		double E1_2 = pow(E1, 2.0);
		double E1_3 = pow(E1, 3.0);
		double E1_4 = pow(E1, 4.0);

		double Z1 = 1.5 * E1 - 27.0 * E1_3 / 32.0;
		double Z2 = 21.0 * E1_2 / 16.0 - 55.0 * E1_4 / 32.0;
		double Z3 = 151.0 * E1_3 / 96.0;
		double Z4 = 1097.0 * E1_4 / 512.0;

		double theta1 = U + Z1 * sin(2.0 * U) + Z2 * sin(4.0 * U) + Z3
				* sin(6.0 * U) + Z4 * sin(8.0 * U);

		double ST2 = pow(sin(theta1), 2.0);

		// o Formula 8-21, page 64
		double C1 = EP2 * pow(cos(theta1), 2.0);

		// o Formula 8-22, page 64
		double T1 = pow(tan(theta1), 2.0);

		// o Formula 8-23, page 64
		double N1 = a / sqrt(1.0 - E2 * ST2);

		// o Formula 8-24, page 64
		double R1 = a * (1.0 - E2) / pow(1.0 - E2 * ST2, 1.5);

		// o Formula 8-25, page 64.
		double D = pp.lon / (N1 * K0);
		double D3 = pow(D, 3.0);
		double D5 = pow(D, 5.0);

		// Formula 8-17, page 63.
		double lat = theta1
				- (N1 * theta1 / R1)
				* (D
						* D
						/ 2.0
						- (5.0 + 3.0 * T1 + 10.0 * C1 - 4.0 * C1 - 4.0 * C1
								* C1 - 9.0 * EP2) * pow(D, 4.0) / 24.0 + (61.0
						+ 90.0 * T1 + 298.0 * C1 + 45.0 * T1 * T1 - 252.0 * EP2 - 3.0
						* C1 * C1)
						* pow(D, 6.0) / 720.0);

		// Calculate Longitude
		double lon = (D - (1.0 + 2.0 * T1 + C1) * D3 / 6.0 + (5.0 - 2.0 * C1
				+ 28 * T1 - 3.0 * C1 * C1 + 8.0 * EP2 + 24.0 * T1 * T1)
				* D5 / 120.0)
				/ cos(T1);

		mp.lat = lat * toDegrees;
		mp.lon = getOriginLon() + lon * toDegrees;
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

	public String toString() {
		return "\t" + getClass().getName() + "\n" + "\tEccentricity="
				+ getEccentricity() + "\n" + "\tOriginLat=" + getOriginLat()
				+ "\n" + "\tOriginLon=" + getOriginLon() + "\n" + "\tRadius="
				+ getRadius() + "\n";
	}
}
