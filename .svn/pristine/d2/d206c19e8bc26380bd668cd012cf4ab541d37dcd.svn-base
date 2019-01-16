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

import java.util.Vector;

public class DataList {

	class DataItem {
		String utm;
		MapPoint mp; // Map point (lat lon).
		MapPoint pp; // Projection point.
	}

	private Vector list = new Vector();

	public static DataList createInstance() {

		return new DataList();
	}

	public void add(double ilat, double ilon, double olat, double olon) {
		DataItem item = new DataItem();

		item.mp = new MapPoint(ilat, ilon);
		item.pp = new MapPoint(olat, olon);

		list.addElement(item);
	}

	public void add(double olat, double olon, String utm) {
		DataItem item = new DataItem();

		item.utm = utm;
		item.pp = new MapPoint(olat, olon);

		list.addElement(item);
	}

	public MapPoint getLatLon(int index) {

		if (index < 0 || index >= list.size()) {
			return null;
		}

		DataItem item = (DataItem) list.elementAt(index);

		return item.mp;
	}

	public MapPoint getProjection(int index) {

		if (index < 0 || index >= list.size()) {
			return null;
		}

		DataItem item = (DataItem) list.elementAt(index);

		return item.pp;
	}

	public int size() {
		return list.size();
	}
}
