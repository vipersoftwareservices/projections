/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
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

package com.viper.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.viper.projections.MapPoint;

public class WDBII {

	private double LARGE = 90.0;
	private double resolution = 60.0 / 3600.0; // default 1 minute;
	private int rank = -1; // default use all ranks
	private int ptCnt = 0;
	private PrintWriter out = null;
	private MapPoint prevPt = new MapPoint(0.0, 0.0);
	private MapPoint currPt = new MapPoint(0.0, 0.0);

	public void setFilename(String filename) {
		try {
			this.out = new PrintWriter(new FileWriter(filename));
		} catch (IOException e) {
			System.out.println("ERROR opening for writing file=" + filename);
			e.printStackTrace();
		}
	}

	public void setResolution(double value) {
		this.resolution = value / 3600.0;
	}

	public void setRank(int value) {
		this.rank = value;
	}

	private double abs(double value) {
		return (value < 0.0) ? -value : value;
	}

	private double sign(double value) {
		return (value < 0.0) ? -1.0 : 1.0;
	}

	private double toDouble(String str) {
		if (str == null || str.length() == 0) {
			return 0.0;
		}
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			System.err.println("toDouble: " + e + ": " + str);
		}
		return 0.0;
	}

	private int toInt(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}

		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			System.err.println("toInt: " + e + ": " + str);
		}
		return 0;
	}

	public void printHeader() {
		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<!DOCTYPE WorldData SYSTEM \"../world.dtd\">");
		out.println("<WorldData>");
		out.println("<Heading>");
		out.println("This file contains points (lats-lons) for the world map.");
		out.println("Athor: TomNevin@pacbell.net");
		out.println("Date:  04/18/2003  - ");
		out.println("</Heading>");
	}

	public void printTrailer() {
		out.println("</WorldData>");
	}

	public void printPt(MapPoint pt) {
		out.println("<Pt lat=\"" + pt.lat + "\" lon=\"" + pt.lon + "\" />");
	}

	public boolean isSame(MapPoint pt1, MapPoint pt2, double resolution) {

		return (abs(pt1.lat - pt2.lat) < resolution && abs(pt1.lon - pt2.lon) < resolution);
	}

	public boolean isLargeDelta(MapPoint pt1, MapPoint pt2) {

		return (abs(pt1.lat - pt2.lat) > LARGE || abs(pt1.lon - pt2.lon) > LARGE);
	}

	public boolean isOutsideLimit(MapPoint pt) {
		boolean inRange = true;
		if (pt.lat > 90.0 || pt.lat < -90.0) {
			inRange = false;
			System.err.println("Outside: " + pt.lat);
		}
		if (pt.lon > 180.0 || pt.lon < -180.0) {
			inRange = false;
			System.err.print("Outside: " + pt.lon);
			pt.lon = ((pt.lon - 180.0) % 360.) + 180.0;
			System.err.println(" changed to: " + pt.lon);
		}
		return inRange;
	}

	public void processPt(String buf) {

		int imid = buf.indexOf(' ');

		String sLat = buf.substring(1, imid);
		String sLon = buf.substring(imid + 1);

		currPt.setLat(toDouble(sLat));
		currPt.setLon(toDouble(sLon));

		isOutsideLimit(currPt);

		if (ptCnt == 0) {
			prevPt.setLat(currPt.getLat());
			prevPt.setLon(currPt.getLon());
			ptCnt = 1;

		} else if (isSame(currPt, prevPt, this.resolution) == false) {

			if (ptCnt == 1) {
				printSegmentStart();
				printGrid();
				printPt(prevPt);
			}

			if (isLargeDelta(currPt, prevPt) == true) {
				System.err.println("CHANGE POINTS: " + currPt + "," + prevPt);

				prevPt.lon = sign(prevPt.lon) * 179.9999999999999;
				printPt(prevPt);

				printSegmentEnd();
				printSegmentStart();
				printGrid();

				prevPt.lat = currPt.lat;
				prevPt.lon = sign(currPt.lon) * 179.9999999999999;
				printPt(prevPt);
				ptCnt = 1;
			}

			printPt(currPt);
			prevPt.setLat(currPt.getLat());
			prevPt.setLon(currPt.getLon());
			ptCnt++;
		}
	}

	public void processSegment(String buf) {
		ptCnt = 0;
	}

	public void printSegmentStart() {
		out.println("<Segment kind=\"land\">");
	}

	public void printSegmentEnd() {
		out.println("</Segment>");
	}

	public void printGrid() {
		out.println("<Grid " + " min_lat=\"-180.0\"" + " max_lat=\"180.0\"" + " min_lon=\"-180.0\""
				+ " max_lon=\"180.0\"/>");
	}

	public void process(String filename) {
		this.ptCnt = 0;

		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));

			while (true) {
				String buf = in.readLine();
				if (buf == null) {
					break;
				}
				if (buf.startsWith("\t") == true) {
					processPt(buf);
				} else if (buf.startsWith("segment") == true) {
					if (this.ptCnt > 1) {
						printSegmentEnd();
					}
					processSegment(buf);
				} else {
					System.err.println("ERROR: " + buf);
				}
			}
			if (this.ptCnt > 1) {
				printSegmentEnd();
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error opening file => " + filename);
			e.printStackTrace(System.err);
		}
	}

	public static void main(String args[]) {

		WDBII map = new WDBII();

		for (int i = 0; i < args.length; i++) {
			if ("-res".equals(args[i]) == true) {
				map.setResolution(map.toDouble(args[++i]));

			} else if ("-rank".equals(args[i]) == true) {
				map.setRank(map.toInt(args[++i]));

			} else if ("-output".equals(args[i]) == true) {
				map.setFilename(args[++i]);
				map.printHeader();

			} else if (args[i].startsWith("-") == false) {
				String filename = args[i];
				map.process(filename);

			}
		}
		map.printTrailer();
	}
}
