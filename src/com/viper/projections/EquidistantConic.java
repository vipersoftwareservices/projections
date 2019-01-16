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

public class EquidistantConic extends MapProjection {

	public EquidistantConic() {
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
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;
		double a = getRadius();

		//  o Formula 16-4.
		double N = (cos(lat1) - cos(lat2)) / (lat2 - lat1);

		//  o Formula 16-3
		double G = cos(lat1) / N + lat1;

		//  o Formula 16-2 
		double P0 = a * (G - lat0);

		//  o Formula 14-4
		double theta = N * (lon - lon0);

		//  o Formula 16-1
		double P = a * (G - lat);

		//  o Formula 14-1
		pp.lon = P * sin(theta);

		//  o Formula 16-6
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

	public void toLatLon(MapPoint pp, MapPoint mp) {

		//  o Convert Degrees to Radians
		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		//  o Formula 16-4.
		double N = (cos(lat1) - cos(lat2)) / (lat2 - lat1);

		//  o Formula 16-3
		double G = cos(lat1) / N + lat1;

		//  o Formula 16-2
		double P0 = a * (G - lat0);

		//  o Formula 14-4
		double theta = atan(lon / (P0 - lat));

		//  o Formula 16-1, Note Radius multiplied later.
		double P = sign(N) * sqrt(lon * lon + pow(P0 - lat, 2.0));

		//  o Formula 14-2
		double Lat = G - P / a;

		//  o Formula 14-1.
		double Lon = lon0 + theta / N;

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;

	}
}
