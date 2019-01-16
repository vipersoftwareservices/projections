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

public class WorldToSVG {

	private double LARGE = 90.0;

	private PrintWriter out = null;

	private double abs(double value) {
		return (value < 0.0) ? -value : value;
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

	private boolean isSame(MapPoint pt1, MapPoint pt2, double resolution) {
		return (abs(pt1.lat - pt2.lat) < resolution && abs(pt1.lon - pt2.lon) < resolution);
	}

	public boolean isLargeDelta(MapPoint pt1, MapPoint pt2) {
		return (abs(pt1.lat - pt2.lat) > LARGE || abs(pt1.lon - pt2.lon) > LARGE);
	}

	private boolean isOutsideLimit(MapPoint pt) {
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

	private void setFilename(String filename) {
		try {
			this.out = new PrintWriter(new FileWriter(filename));
		} catch (IOException e) {
			System.out.println("ERROR opening for writing file=" + filename);
			e.printStackTrace();
		}
	}

	private int printPt(int ptCnt, String buf) {
		int imid = buf.indexOf(' ');

		String ystr = buf.substring(1, imid);
		String xstr = buf.substring(imid + 1);

		int x = (int) (toDouble(xstr) * 3600.0);
		int y = (int) (toDouble(ystr) * 3600.0);

		if (ptCnt == 0) {
			out.print("<path d=\"");
			out.print(" M " + x + " " + y);
		} else {
			out.print(" L " + x + " " + y);
		}
		if ((ptCnt % 5) == 0) {
			out.println("");
		}
		return ptCnt + 1;
	}

	private int printSegmentEnd(int ptCnt) {
		if (ptCnt < 1) {
			return 0;
		}

		String fillColor = "green";
		String strokeColor = "black";
		String strokeWidth = "3";

		out.println(" z\"" + " fill=\"" + fillColor + "\" stroke=\"" + strokeColor + "\" stroke-width=\"" + strokeWidth + "\" />");
		return 0;
	}

	public void processSVG(String filename) {
		System.err.println("Processing file: " + filename);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(filename));

			int ptCnt = 0;
			while (true) {
				String buf = in.readLine();
				if (buf == null)
					break;

				if (buf.startsWith("\t") == true) {
					ptCnt = printPt(ptCnt, buf);
				} else if (buf.startsWith("segment") == true) {
					ptCnt = printSegmentEnd(ptCnt);
				} else {
					System.err.println("ERROR: " + buf);
				}
			}
			printSegmentEnd(ptCnt);

			in.close();
		} catch (Exception e) {
			System.err.println("ERROR file => " + filename + "," + e);
		}
	}

	public void printHeader() {
		out.println("<?xml version=\"1.0\" standalone=\"no\"?>");
		out.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"");
		out.println("\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		out.println("<!--");
		out.println("This file contains points (lats-lons) for the world map.");
		out.println("Athor: TomNevin@pacbell.net");
		out.println("Date:  04/18/2003  - ");
		out.println("-->");
		out.println("\"<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">");
	}

	public void printTrailer() {
		out.println("</svg>");
	}

	public static void main(String args[]) {

		WorldToSVG map = new WorldToSVG();

		for (int i = 0; i < args.length; i++) {
			if ("-res".equals(args[i])) {
				int res = Integer.parseInt(args[++i]);

			} else if ("-output".equals(args[i]) == true) {
				map.setFilename(args[++i]);
				map.printHeader();

			} else if (args[i].startsWith("-") == false) {
				String filename = args[i];
				map.processSVG(filename);
			}
		}
		map.printTrailer();
	}
}
