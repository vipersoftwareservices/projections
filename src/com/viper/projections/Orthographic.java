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

public class Orthographic extends MapProjection {

	public Orthographic() {
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
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 20-3
		pp.lon = a * cos(lat) * sin(lon - lon0);

		//  o Formula 20-4
		pp.lat = a * cos(lat1) * sin(lat) - sin(lat1) * cos(lat)
				* cos(lon - lon0);
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

		double lat = pp.lat;
		double lon = pp.lon;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 20-18
		double P = sqrt(lon * lon + lat * lat);
		if (P == 0.0) {
			mp.lat = lat1 * toDegrees;
			mp.lon = lon0 * toDegrees;
			return;
		}

		//  o Formula 20-19
		double C = asin(P / a);

		//  o Formula 20-14
		double Lat = cos(C) * sin(lat1) + sin(C) * cos(lat1) * lat / P;

		mp.lat = asin(Lat) * toDegrees;

		//  o Formula 20-15
		double num = 0.0;
		double den = 0.0;

		if (getParallel1() == 90.0) {
			num = lon;
			den = -lat;

		} else if (getParallel1() == -90.0) {
			num = lon;
			den = lat;

		} else {
			num = lon * sin(C);
			den = P * cos(lat1) * cos(C) - lat * sin(lat1) * sin(C);
		}

		double Lon = lon0 + atan(num / den);

		mp.lon = Lon * toDegrees;
	}
}
