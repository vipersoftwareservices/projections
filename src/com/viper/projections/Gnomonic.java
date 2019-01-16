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

public class Gnomonic extends MapProjection {

	public Gnomonic() {
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

	public boolean inView(MapPoint mp) {

		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;

		double cosC = sin(lat1) * sin(lat) + cos(lat1) * cos(lat)
				* cos(lon - lon0);

		return (cosC > 0.0);
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

		//  o Convert Degrees to rad.
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		if (getParallel1() == 90.0) {
			pp.lon = a * cot(lat) * sin(lon - lon0);
			pp.lat = -a * cot(lat) * cos(lon - lon0);
			return;

		} else if (getParallel1() == -90.0) {
			pp.lon = -a * cot(lat) * sin(lon - lon0);
			pp.lat = a * cot(lat) * cos(lon - lon0);
			return;

		}

		//  o Formula 5-3.
		double cosC = sin(lat1) * sin(lat) + cos(lat1) * cos(lat)
				* cos(lon - lon0);

		//  o Formula 22-3
		double K = 1.0 / cosC;

		//  o Formula 22-4.
		pp.lon = a * K * cos(lat) * sin(lon - lon0);

		//  o Formula 22-5.
		pp.lat = a
				* K
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

	public void toLatLon(MapPoint pp, MapPoint mp) {

		//  o Convert to Radians
		double lat = pp.lat;
		double lon = pp.lon;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 20-18.
		double P = sqrt(lon * lon + lat * lat);

		//  o Formula 22-16.
		double C = atan(P / a);

		//  o Formula 20-14
		double Lat = lat1;
		if (P != 0.0) {
			Lat = asin(cos(C) * sin(lat1) + lat * sin(C) * cos(lat1) / P);
		}

		//  o Common portion of formulas 20-15, 20-16, and 20-17.
		double num = 0.0;
		double den = 0.0;
		if (lat1 == 90.0) {
			num = lon;
			den = -lat;

		} else if (lat1 == -90.0) {
			num = lon;
			den = lat;

		} else {
			num = lon * sin(C);
			den = P * cos(lat1) * cos(C) - lat * sin(lat1) * sin(C);

		}

		double Lon = lon0 + atan(num / den);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	}
}
