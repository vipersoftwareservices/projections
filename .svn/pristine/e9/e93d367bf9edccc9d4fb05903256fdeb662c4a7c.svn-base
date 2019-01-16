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

public class EquidistantCylindrical extends MapProjection {

	public EquidistantCylindrical() {
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

		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 12-1
		pp.lat = lat * a;
		pp.lon = (lon - lon0) * cos(lat1) * a;
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

		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 12-5
		mp.lat = (lat / a) * toDegrees;

		//  o Formula 12-6.
		double Lon = (lon0 + lon / (a * cos(lat1))) * toDegrees;

		mp.lon = ((Lon - 180.0) % 360.0) + 180.0;
	}
}
