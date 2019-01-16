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

public class Stereographic extends MapProjection {

	double K0 = 1.0;

	public Stereographic() {
	}

	public double getK0() {
		return this.K0;
	}

	public void setK0(double K0) {
		this.K0 = K0;
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

		//  o Convert Degrees to Radians
		double lat = mp.lat * toRadians;
		double lon = (mp.lon - getOriginLon()) * toRadians;
		double SP = getParallel1() * toRadians;

		//  o Formula 21-4
		double R0 = 1.0 + sin(SP) * sin(lat) + cos(SP) * cos(lat) * cos(lon);

		double K = 2.0 * this.K0 / R0;

		//  o Formula 21-2
		pp.lon = getRadius() * K * cos(lat) * sin(lon);

		//  o Formula 21-3
		double RY = cos(SP) * sin(lat) - sin(SP) * cos(lat) * cos(lon);

		pp.lat = getRadius() * K * RY;
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

		//  o Convert Degrees to Radians 
		double lat = pp.lat;
		double lon = pp.lon;
		double SP1 = getParallel1() * toRadians;

		//  o Formula 20-18
		double P = sqrt(lon * lon + lat * lat);

		if (P == 0.0) {
			mp.lat = getParallel1();
			mp.lon = getOriginLon();
			new Exception().printStackTrace();
			return;
		}

		//  o Formula 21-15, Denominator always positive.
		double C = 2.0 * atan(P / (2.0 * getRadius() * this.K0));

		//  o Formula 20-14
		double RY = cos(C) * sin(SP1) + lat * sin(C) * cos(SP1) / P;

		mp.lat = asin(RY) * toDegrees;

		//  o Formula 20-15
		double RD = P * cos(SP1) * cos(C) - lat * sin(SP1) * sin(C);

		double RN = lon * sin(C);

		//  o Common portion of formulas 20-15, 20-16, and 20-17.
		double RX = getOriginLon() + atan2(RN, RD) * toDegrees;

		mp.lon = ((RX - 180.0) % 360.0) + 180.0;
	}
}
