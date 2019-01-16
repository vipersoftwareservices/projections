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

public class UTM extends MapProjection {

	TransverseMercator tm = new TransverseMercator();

	public UTM() {
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

	public void setNAD27(MapPoint center) {

		//--------------------------------------------------------------
		//  o Generate Origin
		//--------------------------------------------------------------

		int ZONE = 30 - (int) (center.lon / 6.0);
		int CM = 183 - 6 * ZONE;

		//--------------------------------------------------------------
		//  o Generate Eccentricity and Flattening
		//--------------------------------------------------------------

		double R = 6378206.4;
		double F = 1.0 / 294.978698;
		double E = F + F - F * F;

		setOriginLon((double) CM);
		setOriginLat(0.0);
		setEccentricity(E);
		setRadius(R);
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

	public void setNAD83(MapPoint center) {

		//--------------------------------------------------------------
		//  o Generate Origin
		//--------------------------------------------------------------

		int ZONE = 30 - (int) (center.lon / 6.0);
		int CM = 183 - 6 * ZONE;

		//--------------------------------------------------------------
		//  o Generate Eccentricity and Flattening
		//--------------------------------------------------------------

		double R = 6378137.0;
		double F = 1.0 / 298.257222101;
		double E = F + F - F * F;

		setOriginLon((double) CM);
		setOriginLat(0.0);
		setEccentricity(E);
		setRadius(R);
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

		double R, F, E;

		//--------------------------------------------------------------
		//  o Generate Origin
		//--------------------------------------------------------------

		int ZONE = 30 - (int) (mp.lon / 6.0);
		int CM = 183 - 6 * ZONE;

		double lon = (double) CM;
		double lat = 0.0;

		//--------------------------------------------------------------
		//  o Generate Eccentricity and Flattening
		//--------------------------------------------------------------

		//
		//  o NAD 27
		//
		R = 6378206.4;
		F = 1.0 / 294.978698;
		E = F + F - F * F;

		//
		//  o NAD 83
		//
		R = 6378137.0;
		F = 1.0 / 298.257222101;
		E = F + F - F * F;

		//--------------------------------------------------------------
		//  o Project to Transverse Mercator
		//--------------------------------------------------------------

		tm.setOriginLat(lat);
		tm.setOriginLon(lon);
		tm.setEccentricity(E);
		tm.setRadius(R);

		tm.toProjection(mp, pp);
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

		double R, F, E;

		//--------------------------------------------------------------
		//  o Generate Origin
		//--------------------------------------------------------------

		int ZONE = 30 - (int) (getOriginLon() / 6.0);
		int CM = 183 - 6 * ZONE;

		double olon = (double) CM;
		double olat = 0.0;

		//--------------------------------------------------------------
		//  o Generate Eccentricity and Flattening
		//--------------------------------------------------------------
		//
		//  o NAD 27
		//
		R = 6378206.4;
		F = 1.0 / 294.978698;
		E = F + F - F * F;

		//
		//  o NAD 83
		//
		R = 6378137.0;
		F = 1.0 / 298.257222101;
		E = F + F - F * F;

		//--------------------------------------------------------------
		//  o Project to Transverse Mercator
		//--------------------------------------------------------------

		tm.setOriginLat(olat);
		tm.setOriginLon(olon);
		tm.setEccentricity(E);
		tm.setRadius(R);

		tm.toLatLon(pp, mp);
	}

	/** ----------------------------------------------------------
	 **
	 ** Example: 32U LA 1234 1234
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toTransvereMercator(String utm, MapPoint tm) {

		double LAT, LON;

		//
		// Pull out Longitude Grid Designator 1.
		//
		int index1 = 0;
		int index2 = 0;
		for (int i = 0; i < utm.length(); i++) {
			char c = utm.charAt(i);
			if (c < '0' || c > '9') {
				break;
			}
			index2 = i;
		}

		int LON_GRID_1 = toInt(utm.substring(index1, index2));

		//
		// Pull out Latitude Grid Designator 1.
		//
		index1 = index2 + 1;
		int LAT_GRID_1 = (int) (utm.charAt(index1) - 'A');

		//
		// Pull out Longitude Grid Designator 2.
		//
		index1 = index1 + 1;
		int LON_GRID_2 = (int) (utm.charAt(index1) - 'A');

		//
		// Pull out Latitude Grid Designator 2.
		//
		index1 = index1 + 1;
		int LAT_GRID_2 = (int) (utm.charAt(index1) - 'A');

		//
		// Determine Depth of Designator 3.
		//
		index1 = index1 + 1;
		int size = utm.length() - index1 + 1;

		if ((size & 2) == 1) {
			new Exception("UTM_TO_TM.STRING SIZE ERROR").printStackTrace();
		}

		size = size / 2;

		//
		// Pull out Longitude Grid Designator 3.
		//
		index2 = index1 + size - 1;
		int LON_GRID_3 = toInt(utm.substring(index1, index2));

		//
		// Pull out Latitude Grid Designator 3.
		//
		index1 = index2 + 1;
		index2 = utm.length();
		int LAT_GRID_3 = toInt(utm.substring(index1, index2));

		//
		// Deal with Polar Regions.
		//
		if (LAT_GRID_1 < 2 || LAT_GRID_1 > 23) {
			new Exception("UTM_TO_TM.OUTSIDE_CENTRAL_REGION_ERROR.")
					.printStackTrace();
		}

		//
		// Determine the Zone Origin
		//
		if (LAT_GRID_1 > 7) {
			LAT_GRID_1 = LAT_GRID_1 - 1;
			if (LAT_GRID_1 > 12) {
				LAT_GRID_1 = LAT_GRID_1 - 1;
			}
		}

		if (LAT_GRID_1 > 2 && LAT_GRID_1 < 21) {
			LAT = (double) ((LAT_GRID_1 - 2) * 8 - 72);
		} else {
			LAT = (double) ((LAT_GRID_1 - 2) * 8 - 84);
		}

		LON = (double) (((LON_GRID_1 - 1) * 6) - 177);

		//
		// Special Cases.
		//
		if (LON == 3.0 && LAT == 56.0) {
			LON = 1.5;
		} else if (LON == 9.0 && LAT == 56.0) {
			LON = 7.5;
		} else if (LON == 3.0 && LAT == 72.0) {
			LON = 4.5;
		} else if (LON == 9.0 && LAT == 72.0) {
			LON = 4.5;
		} else if (LON == 15.0 && LAT == 72.0) {
			LON = 15.0;
		} else if (LON == 21.0 && LAT == 72.0) {
			LON = 15.0;
		} else if (LON == 27.0 && LAT == 72.0) {
			LON = 27.0;
		} else if (LON == 33.0 && LAT == 72.0) {
			LON = 27.0;
		} else if (LON == 39.0 && LAT == 72.0) {
			LON = 37.5;
		}
	}

	/** ----------------------------------------------------------
	 **
	 ** Example: 32U LA 1234 1234
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public String toUTM(MapPoint tm) {
		return null;
	}
}
