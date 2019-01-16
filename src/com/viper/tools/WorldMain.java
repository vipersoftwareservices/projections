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

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class WorldMain {
	public static Map _cities = new HashMap(30000);

	public static final double LARGE = 90.0;

	public static final int ptsPerLine = 10;

	public static final double SMALLEST_RESOLUTION = 0.0000001;

	public static void main(String args[]) {
		WorldMain map = new WorldMain();
		PrinterInterface printer = null;
		List coastlines = new ArrayList();

		for (int i = 0; i < args.length; i++) {
			System.out.println("Processing args :" + args[i]);
			if (args[i].startsWith("-svg")) {
				double resolution = Segment.toDouble(args[++i]);
				String filename = args[++i];
				printer = new SVGPrinter(resolution, filename);
				printer.printHeader();
			} else if (args[i].equals("-boundary") == true) {
				map.processBorders(printer, args[++i], CoastlineSegment.POLITICAL, coastlines);
			} else if (args[i].equals("-coastline") == true) {
				map.processCoastline(printer, args[++i], CoastlineSegment.COASTLINE);
			} else if (args[i].equals("-river") == true) {
				map.processCoastline(printer, args[++i], Segment.RIVER);
			} else if (args[i].equals("-states") == true) {
				map.processBorders(printer, args[++i], CoastlineSegment.STATES, coastlines);
			} else if (args[i].equals("-cities") == true) {
				map.processCities(printer, args[++i]);
			} else if (args[i].equals("-labels") == true) {
				map.processLabels(printer, args[++i]);
			} else if (args[i].equals("-connections") == true) {
				map.processConnections(printer, args[++i]);
			}
		}
		printer.printTrailer();

		Statistics.printTotals();
	}

	public List buildSegments(String filename, String kind) {
		BufferedReader in = null;
		List list = new ArrayList();

		try {
			in = new BufferedReader(new FileReader(filename));

			while (true) {
				String buf = in.readLine();
				if (buf == null)
					break;

				if (CoastlineSegment.isSegment(buf) == false) {
					throw new Exception("Segments out of whack: " + buf);
				}

				CoastlineSegment seg = new CoastlineSegment(buf, kind);
				list.add(seg);

				if ((list.size() % 10) == 0) {
					System.err.print(".");
				}
				if ((list.size() % 1000) == 0) {
					System.err.println("");
				}

				for (int i = 0; i < seg.npts; i++) {
					String ptstr = in.readLine();
					if (ptstr == null)
						break;
					if (CoastlineSegment.isPoint(ptstr) == false) {
						throw new Exception("Points out of whack: " + ptstr);
					}
					seg.addPoint(ptstr);
				}
			}
			in.close();
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + e);
			e.printStackTrace(System.err);
		}
		System.err.println("");
		return list;
	}

	public void connectSequentialSegments(List list, double resolution) {
		for (int i1 = 0; i1 < list.size() - 1; i1++) {
			CoastlineSegment seg1 = (CoastlineSegment) list.get(i1);
			if (seg1.isConnected())
				continue;

			for (int i2 = i1 + 1; i2 < list.size();) {
				CoastlineSegment seg2 = (CoastlineSegment) list.get(i2);
				if (seg2.isConnected()) {
					break;
				}

				WorldPoint pt1A = seg1.getFirstPoint();
				WorldPoint pt1Z = seg1.getLastPoint();
				WorldPoint pt2A = seg2.getFirstPoint();
				WorldPoint pt2Z = seg2.getLastPoint();

				if (pt1A.isSame(pt2A, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1A.isSame(pt2Z, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg2.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1Z.isSame(pt2A, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1Z.isSame(pt2Z, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg2.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else {
					break;
				}

				if (seg1.isConnected())
					break;
			}
		}
	}

	public void connectSegments(List list, double resolution) {
		for (int i1 = 0; i1 < list.size() - 1; i1++) {
			CoastlineSegment seg1 = (CoastlineSegment) list.get(i1);
			if (seg1.isConnected())
				continue;

			for (int i2 = i1 + 1; i2 < list.size();) {
				CoastlineSegment seg2 = (CoastlineSegment) list.get(i2);
				if (seg2.isConnected()) {
					i2++;
					continue;
				}

				WorldPoint pt1A = seg1.getFirstPoint();
				WorldPoint pt1Z = seg1.getLastPoint();
				WorldPoint pt2A = seg2.getFirstPoint();
				WorldPoint pt2Z = seg2.getLastPoint();

				if (pt1A.isSame(pt2A, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1A.isSame(pt2Z, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg2.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1Z.isSame(pt2A, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else if (pt1Z.isSame(pt2Z, resolution)) {
					System.out.println("CONNECT: " + seg1.segment + ", " + seg2.segment);
					seg2.reverse();
					seg1.add(seg2);
					list.remove(i2);
					i2 = i1 + 1;
				} else {
					i2++;
				}

				if (seg1.isConnected())
					break;
			}
		}
	}

	public int connectOverlappingSegments(List list, double resolution) {
		int connected = 0;
		for (int i1 = 0; i1 < list.size() - 1; i1++) {
			CoastlineSegment seg1 = (CoastlineSegment) list.get(i1);
			if (seg1.isConnected())
				continue;

			for (int i2 = i1 + 1; i2 < list.size();) {
				CoastlineSegment seg2 = (CoastlineSegment) list.get(i2);
				if (seg2.isConnected() || seg2.intersects(seg1) == false) {
					i2++;
					continue;
				}

				List pts1 = seg1.getPoints();
				List pts2 = seg2.getPoints();

				int index;
				index = CoastlineSegment.matchHeadToHead(pts1, pts2, resolution);
				if (index != -1) {
					System.err.println("matchHeadToHead: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg1.add(seg2, index + 1);
					list.remove(i2);
					connected++;
					i2 = i1 + 1;
					continue;
				}
				index = CoastlineSegment.matchHeadToTail(pts1, pts2, resolution);
				if (index != -1) {
					System.err.println("matchHeadToTail: " + seg1.segment + ", " + seg2.segment);
					seg1.reverse();
					seg2.reverse();
					seg1.add(seg2, pts2.size() - index);
					list.remove(i2);
					connected++;
					i2 = i1 + 1;
					continue;
				}
				index = CoastlineSegment.matchTailToHead(pts1, pts2, resolution);
				if (index != -1) {
					System.err.println("matchTailToHead: " + seg1.segment + ", " + seg2.segment);
					seg1.add(seg2, index + 1);
					list.remove(i2);
					connected++;
					i2 = i1 + 1;
					continue;
				}
				index = CoastlineSegment.matchTailToTail(pts1, pts2, resolution);
				if (index != -1) {
					System.err.println("matchTailToTail: " + seg1.segment + ", " + seg2.segment);
					seg2.reverse();
					seg1.add(seg2, pts2.size() - index);
					list.remove(i2);
					connected++;
					i2 = i1 + 1;
					continue;
				}
				if (seg1.isConnected())
					break;

				i2++;
			}
		}
		return connected;
	}

	public void connectBorderSegments(List list, double resolution, List extras) {
		for (int i = 0; i < list.size(); i++) {
			CoastlineSegment seg = (CoastlineSegment) list.get(i);
			if (seg.isConnected())
				continue;
			for (int j = 0; j < extras.size(); j++) {
				List extraList = (List) extras.get(j);
				for (int k = 0; k < extraList.size(); j++) {
					CoastlineSegment extra = (CoastlineSegment) extraList.get(k);

					if (extra.getExtents().contains(seg.getExtents()) == false)
						continue;

					WorldPoint ptA = seg.getFirstPoint();
					int ptAIndex = CoastlineSegment.find(ptA, seg.getPoints(), resolution);
					if (ptAIndex != -1) {
						System.out.println(seg.segment + " uses " + extra.segment + " index 1st pt at " + ptAIndex);

					}

					WorldPoint ptB = seg.getFirstPoint();
					int ptBIndex = CoastlineSegment.find(ptB, seg.getPoints(), resolution);
					if (ptBIndex != -1) {
						System.out.println(seg.segment + " uses " + extra.segment + " index last pt at " + ptBIndex);
					}
				}
			}
		}
	}

	private void printSortedSegments(List list, double resolution) {
		TreeMap map = new TreeMap();
		for (int i = 0; i < list.size(); i++) {
			CoastlineSegment seg = (CoastlineSegment) list.get(i);
			map.put(new SortKey(seg.getFirstPoint(), seg.segment), seg);
			map.put(new SortKey(seg.getLastPoint(), seg.segment), seg);
		}

		Iterator itr = map.keySet().iterator();
		while (itr.hasNext()) {
			Object key = itr.next();
			CoastlineSegment seg = (CoastlineSegment) map.get(key);

			if (seg != null && seg.isConnected(resolution) == false) {
				System.out.println(seg.segment + ", " + seg.getFirstPoint() + ", " + ", " + seg.getLastPoint() + "," + seg.npts);
			}
		}
	}

	public void splitDateLine(List list, double sign) {
		for (int i = 0; i < list.size(); i++) {
			CoastlineSegment seg = (CoastlineSegment) list.get(i);
			List pts = seg.pts;

			boolean crossDateline = false;
			for (int j = 1; j < pts.size(); j++) {
				WorldPoint pt1 = (WorldPoint) pts.get(j - 1);
				WorldPoint pt2 = (WorldPoint) pts.get(j);
				if (pt1.isSame(pt2, LARGE) == false) {
					crossDateline = true;
					break;
				}
			}
			if (crossDateline == true) {
				for (int j = 0; j < pts.size(); j++) {
					WorldPoint pt = (WorldPoint) pts.get(j);
					if (sign > 0.0) {
						if (pt.lon < 0.0) {
							System.err.println("DATELINE: " + pt);
							pt.lon = (pt.lon % 360.0) + 360.0;
						}
					} else {
						if (pt.lon > 0.0) {
							System.err.println("DATELINE: " + pt);
							pt.lon = (pt.lon % 360.0) - 360.0;
						}
					}
				}
			}
		}
	}

	public void connectAntartica(List list) {
		for (int i = 0; i < list.size(); i++) {
			CoastlineSegment seg = (CoastlineSegment) list.get(i);

			if (seg.segment == 1874) {
				WorldPoint firstPt = seg.getFirstPoint();
				WorldPoint lastPt = seg.getLastPoint();
				if (lastPt.lon < 0.0) {
					seg.add(new WorldPoint(lastPt.lat, -180.0));
					seg.add(new WorldPoint(-90.0, -180.0));
					seg.add(new WorldPoint(-90.0, 0.0));
					seg.add(new WorldPoint(-90.0, 180.0));
					seg.add(new WorldPoint(firstPt.lat, 180.0));
				} else {
					seg.add(new WorldPoint(lastPt.lat, 180.0));
					seg.add(new WorldPoint(-90.0, 180.0));
					seg.add(new WorldPoint(-90.0, 0.0));
					seg.add(new WorldPoint(-90.0, -180.0));
					seg.add(new WorldPoint(firstPt.lat, -180.0));
				}
			}
		}
	}

	public void connectAsia(List list) {
		CoastlineSegment seg = null;

		// Connect Europe
		// [java] NOT CONNECTED, Segment 1.
		seg = new CoastlineSegment(-1);
		seg.add(new WorldPoint(44.6225, 28.832778));
		seg.add(new WorldPoint(54.343056, 19.004444));
		list.add(seg);

		// [java] NOT CONNECTED, Segment 49.
		seg = new CoastlineSegment(-49);
		seg.add(new WorldPoint(60.560556, 27.860833));
		seg.add(new WorldPoint(69.79, 30.784444));
		list.add(seg);

		// Connect Africa
		// [java] NOT CONNECTED, Segment 6.
		seg = new CoastlineSegment(-6);
		seg.add(new WorldPoint(40.975833, 39.997222));
		seg.add(new WorldPoint(25.635556, 56.269722));
		list.add(seg);
	}

	public void connectNorthAmerica(List list) {
		CoastlineSegment seg = null;

		// Connect Africa
		// [java] NOT CONNECTED, Segment 6.
		seg = new CoastlineSegment(-6);
		seg.add(new WorldPoint(25.968889, -97.136667));
		seg.add(new WorldPoint(33.003333, -117.281667));
		list.add(seg);
	}

	public void generateLakes(List<CoastlineSegment> list) {
		for (int i = 0; i < (list.size() - 1); i++) {
			CoastlineSegment seg1 = list.get(i);
			WorldRectangle r1 = seg1.getExtents();

			for (int j = i + 1; j < list.size(); j++) {
				CoastlineSegment seg2 = list.get(j);
				WorldRectangle r2 = seg2.getExtents();

				if (r1.contains(r2)) {
					seg2.kind = Segment.LAKE;
				} else if (r2.contains(r1)) {
					seg1.kind = Segment.LAKE;
				}
			}
		}
	}

	private void removeSmallItems(List list, double resolution) {
		for (int i = list.size() - 1; i >= 0; i--) {
			Segment seg = (Segment) list.get(i);
			WorldRectangle r = seg.getExtents();
			if (r == null) {
				continue;
			}
			List pts = seg.getPoints();
			if (pts == null)
				continue;
			if (pts.size() <= 1) {
				list.remove(i);
			} else if (r.isSmaller(resolution)) {
				list.remove(i);
			}
		}
	}

	public List processCoastline(PrinterInterface printer, String filename, String kind) {
		System.out.println("Processing: " + filename);
		Statistics stats = new Statistics(filename);
		List list = null;

		try {
			System.out.println("Fetch raw segments...");
			list = buildSegments(filename, kind);

			System.out.println("Connect at international dateline...");
			double sign = (filename.endsWith("namer-cil.txt")) ? -1.0 : 1.0;
			splitDateLine(list, sign);

			System.out.println("Connect asia...");
			if (filename.endsWith("asia-cil.txt")) {
				connectAsia(list);
			}

			System.out.println("Connect north america...");
			if (filename.endsWith("namer-cil.txt")) {
				connectNorthAmerica(list);
			}

			System.out.println("Connect segments (sequential )...");
			connectSequentialSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segments (generalized)...");
			connectSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segments (overlapping)...");
			connectOverlappingSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segments (broadbased)...");
			connectSegments(list, 0.5); // TODO 0.5

			System.out.println("Connect antartica...");
			if (filename.endsWith("samer-cil.txt")) {
				connectAntartica(list);
			}

			System.out.println("Generate lakes...");
			if (filename.endsWith("-cil.txt")) {
				generateLakes(list);
			}

			System.out.println("Remove small land items...");
			removeSmallItems(list, 0.1);

			if (kind == CoastlineSegment.COASTLINE) {
				System.out.println("Final connect ....................................");
				printSortedSegments(list, 0.5);

				System.out.println("Final statistics .................................");
				stats.printStatistics(list, 0.5);
			}

			printer.printBody(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List processBorders(PrinterInterface printer, String filename, String kind, List coastlines) {
		System.out.println("Processing: " + filename);
		List list = null;

		try {
			System.out.println("Fetch raw segments...");
			list = buildSegments(filename, kind);

			System.out.println("Split the segements at the dateline...");
			splitDateLine(list, 1.0);

			System.out.println("Connect segments (sequential)...");
			connectSequentialSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segments (generalized)...");
			connectSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segments (overlapping)...");
			connectOverlappingSegments(list, SMALLEST_RESOLUTION);

			System.out.println("Connect segmenst (with other segments)...");
			connectBorderSegments(list, SMALLEST_RESOLUTION, coastlines);

			printer.printBody(list);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void processLabels(PrinterInterface printer, String filename) {
		List list = LabelSegment.build(filename);

		printer.printBody(list);
	}

	public void processConnections(PrinterInterface printer, String filename) {
		List list = buildConnections(filename);
		printer.printBody(list);
	}

	public List buildConnections(String filename) {
		BufferedReader in = null;
		List list = new ArrayList();
		int lineno = 0;

		try {
			in = new BufferedReader(new FileReader(filename));

			while (true) {
				String buf = in.readLine();
				if (buf == null)
					break;

				lineno++;

				ConnectionSegment connect = new ConnectionSegment(buf);
				list.add(connect);
			}
			in.close();
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + lineno + ", " + e);
			e.printStackTrace(System.err);
		}
		System.err.println("");
		return list;
	}

	public void processCities(PrinterInterface printer, String filename) {
		List list = buildCities(filename);

		for (int i = 0; i < list.size(); i++) {
			CitySegment city = (CitySegment) list.get(i);
			_cities.put(city.cityName, city);
		}
	}

	public List buildCities(String filename) {
		BufferedReader in = null;
		List list = new ArrayList();
		int lineno = 0;

		try {
			in = new BufferedReader(new FileReader(filename));

			while (true) {
				String buf = in.readLine();
				if (buf == null)
					break;

				lineno++;
				if (lineno < 5)
					continue;

				CitySegment city = new CitySegment(buf);
				list.add(city);
			}
			in.close();
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + lineno + ", " + e);
			e.printStackTrace(System.err);
		}
		System.err.println("");
		return list;
	}
}

// ---------------------------------------------------------------------------------------------
// Sort Key
// ---------------------------------------------------------------------------------------------

class SortKey implements Comparable {
	WorldPoint pt;

	int segment;

	public SortKey(WorldPoint pt, int segment) {
		this.pt = pt;
		this.segment = segment;
	}

	public int compareTo(Object o) {
		SortKey other = (SortKey) o;
		int comparison = pt.compareTo(other.pt);
		if (comparison != 0)
			return comparison;

		return segment - other.segment;
	}
}

// ---------------------------------------------------------------------------------------------
// World Point
// ---------------------------------------------------------------------------------------------

class WorldPoint implements Comparable {

	double lat = 0.0;

	double lon = 0.0;

	public WorldPoint(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public int compareTo(Object o) {
		WorldPoint other = (WorldPoint) o;
		double dx = this.lon - other.lon;
		if (dx == 0.0) {
			dx = this.lat - other.lat;
		}
		return (int) dx;
	}

	private double abs(double value) {
		return (value < 0.0) ? -value : value;
	}

	private boolean sameSign(double value1, double value2) {
		if (value1 >= 0.0 && value2 >= 0.0)
			return true;
		if (value1 <= 0.0 && value2 <= 0.0)
			return true;
		return false;
	}

	public boolean isSame(WorldPoint pt1, double resolution) {
		if (pt1 == null)
			return false;
		double dlat = abs(pt1.lat - lat);
		if (dlat > resolution)
			return false;
		double dlon = abs(pt1.lon - lon);
		if (dlon < resolution) {
			return true;
		}
		if (sameSign(pt1.lon, lon) == false)
			return false;

		double dist = abs(ConnectionSegment.getDistance0(pt1.lat, pt1.lon, lat, lon));
		return (dist < resolution);
	}

	public String toString() {
		return "[lat=" + lat + "],[lon=" + lon + "]";
	}
}

// ---------------------------------------------------------------------------------------------
// World Rectangle
// ---------------------------------------------------------------------------------------------

class WorldRectangle {
	double minlat = 0.0;

	double minlon = 0.0;

	double maxlat = 0.0;

	double maxlon = 0.0;

	public WorldRectangle(double minlat, double maxlat, double minlon, double maxlon) {
		this.minlat = minlat;
		this.maxlat = maxlat;
		this.minlon = minlon;
		this.maxlon = maxlon;
	}

	public void add(WorldPoint pt) {
		minlat = Math.min(minlat, pt.lat);
		maxlat = Math.max(maxlat, pt.lat);
		minlon = Math.min(minlon, pt.lon);
		maxlon = Math.max(maxlon, pt.lon);
	}

	public boolean intersects(WorldRectangle r) {
		return !((r.maxlat <= minlat) || (r.maxlon <= minlon) || (r.minlat >= maxlat) || (r.minlon >= maxlon));
	}

	public boolean contains(WorldRectangle r) {
		return contains(r.minlat, r.minlon, r.maxlat, r.maxlon);
	}

	public boolean contains(double minlat, double minlon, double maxlat, double maxlon) {
		return (minlon >= this.minlon && minlat >= this.minlat && maxlon <= this.maxlon && maxlat <= this.maxlat);
	}

	private double abs(double value) {
		return (value < 0.0) ? -value : value;
	}

	public boolean isSmaller(double resolution) {
		double dlat = abs(minlat - maxlat);
		double dlon = abs(minlon - maxlon);
		return (dlat < resolution && dlon < resolution);
	}
}

// ---------------------------------------------------------------------------------------------
// Segment
// ---------------------------------------------------------------------------------------------

abstract class Segment {
	public static final String WATER = "water";

	public static final String RIVER = "river";

	public static final String CITY = "city";

	public static final String ICE = "ice";

	public static final String LAKE = "lake";

	public static final String COASTLINE = "coastline";

	public static final String POLITICAL = "political";

	public static final String STATES = "state"; // State boundaries

	public static final String TEXT = "text";

	public static final String CONNECT = "connect";

	public String kind = WATER;

	public transient static DecimalFormat decimal = new DecimalFormat();
	static {
		decimal.setMaximumFractionDigits(6);
		decimal.setMinimumFractionDigits(6);
		decimal.setGroupingUsed(false);
	}

	public abstract void printSVG(PrintWriter out, double resolution);

	public static int toInt(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
			System.err.println("ERROR: toInt: " + e + ": " + str);
			e.printStackTrace(System.err);
		}
		return 0;
	}

	public static double toDouble(String str) {
		if (str == null || str.length() == 0) {
			return 0.0;
		}
		try {
			return Double.parseDouble(str.trim());
		} catch (Exception e) {
			System.err.println("ERROR: toDouble: " + e + ": " + str);
			e.printStackTrace(System.err);
		}
		return 0.0;
	}

	public long toLong(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		try {
			str = str.trim();
			if (str.startsWith("+")) {
				str = str.substring(1);
			}
			return Long.parseLong(str);
		} catch (Exception e) {
			System.err.println("ERROR: toLong: " + e + ": " + str);
			e.printStackTrace(System.err);
		}
		return 0;
	}

	public WorldRectangle getExtents() {
		return null;
	}

	public List getPoints() {
		return null;
	}

	public String getStyle() {
		if (WATER.equals(kind))
			return "&style-water;";
		if (ICE.equals(kind))
			return "&style-ice;";
		if (TEXT.equals(kind))
			return "&style-text;";
		if (CONNECT.equals(kind))
			return "&style-connect;";
		if (RIVER.equals(kind))
			return "&style-river;";
		if (LAKE.equals(kind))
			return "&style-lake;";
		if (CITY.equals(kind))
			return "&style-city;";
		if (COASTLINE.equals(kind))
			return "&style-land;";
		if (POLITICAL.equals(kind))
			return "&style-border;";
		if (STATES.equals(kind))
			return "&style-border;";
		return "&style-default;";
	}
}

// ---------------------------------------------------------------------------------------------
// City Segment
// ---------------------------------------------------------------------------------------------

class CitySegment extends Segment {
	public int stateCode = 0;

	public int placeCode = 0;

	public String cityName = null;

	public String stateAbbrev = null;

	public double lon;

	public double lat;

	public CitySegment(String str) {
		stateCode = toInt(str.substring(1, 3));
		placeCode = toInt(str.substring(4, 9));
		cityName = str.substring(10, 35).trim();
		stateAbbrev = str.substring(36, 38);
		lat = toLong(str.substring(39, 48)) / 1000000.0;
		lon = toLong(str.substring(49, 59)) / 1000000.0;
		this.kind = CITY;
	}

	public void printSVG(PrintWriter out, double resolution) {
		out.print("<text x='" + lon + "' y='" + (90.0 - lat) + "' style=\"&style-city;\" >");
		out.print(cityName.trim());
		out.print("</text>\n");
	}
}

// ---------------------------------------------------------------------------------------------
// Label Segment
// ---------------------------------------------------------------------------------------------

class LabelSegment extends Segment {
	public String text = null;

	public String category = "general";

	public double lon;

	public double lat;

	public LabelSegment(String text, String category, String lat, String lon) {
		this.text = text.trim();
		this.category = category;
		this.lat = toDouble(lat);
		this.lon = toDouble(lon);
	}

	public LabelSegment(String str) {
		StringTokenizer tokens = new StringTokenizer(str, " /t/n");

		int index = 0;
		while (tokens.hasMoreTokens()) {
			index = index + 1;
			switch (index) {
			case 1:
				this.lat = toDouble(tokens.nextToken());
				break;
			case 2:
				this.lon = toDouble(tokens.nextToken());
				break;
			case 3:
				this.category = tokens.nextToken();
				break;
			case 4:
				this.text = tokens.nextToken().trim();
				break;
			}
		}
	}

	public static List build(String filename) {
		BufferedReader in = null;
		List list = new ArrayList();
		int lineno = 0;

		try {
			in = new BufferedReader(new FileReader(filename));

			while (true) {
				String str = in.readLine();
				if (str == null)
					break;

				lineno++;

				list.add(new LabelSegment(str));
			}
			in.close();
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + lineno + ", " + e);
			e.printStackTrace(System.err);
		}
		System.err.println("");
		return list;
	}

	public void printSVG(PrintWriter out, double resolution) {
		out.print("<text x='" + lon + "' y='" + (90.0 - lat) + "' style=\"&style-label;\" >");
		out.print(text);
		out.print("</text>\n");
	}
}

// ---------------------------------------------------------------------------------------------
// Connection Segment
// ---------------------------------------------------------------------------------------------

class ConnectionSegment extends Segment {
	public CitySegment city0 = null;

	public CitySegment city1 = null;

	public WorldPoint pt0 = new WorldPoint(0.0, 0.0);

	public WorldPoint pt1 = new WorldPoint(0.0, 0.0);

	public final double StandardRadius = 6370997.0;

	public ConnectionSegment(String str) {
		int i = str.indexOf("->");
		String name0 = str.substring(0, i).trim();
		String name1 = str.substring(i + 2).trim();

		city0 = (CitySegment) WorldMain._cities.get(name0);
		city1 = (CitySegment) WorldMain._cities.get(name1);

		pt0 = new WorldPoint(city0.lat, city0.lon);
		pt1 = new WorldPoint(city1.lat, city1.lon);
	}

	public static double getDistance(WorldPoint pt0, WorldPoint pt1) {

		double lat0 = Math.toRadians(pt0.lat);
		double lat1 = Math.toRadians(pt1.lat);
		double dlon = Math.toRadians(Math.abs(pt0.lon - pt1.lon));
		double d = Math.sin(lat0) * Math.sin(lat1) + Math.cos(lat0) * Math.cos(lat1) * Math.cos(dlon);
		double distance = Math.toDegrees(Math.acos(d));

		return distance;
	}

	public static double getDistance(double lat0, double lon0, double lat1, double lon1) {

		double lat0r = Math.toRadians(lat0);
		double lat1r = Math.toRadians(lat1);
		double dlon = Math.toRadians(Math.abs(lon0 - lon1));
		double d = Math.sin(lat0r) * Math.sin(lat1r) + Math.cos(lat0r) * Math.cos(lat1r) * Math.cos(dlon);
		double distance = Math.toDegrees(Math.acos(d));

		return distance;
	}

	public static double getDistance0(double lat0, double lon0, double lat1, double lon1) {

		double lat0r = Math.toRadians(lat0);
		double lat1r = Math.toRadians(lat1);
		double lon0r = Math.toRadians(lon0);
		double lon1r = Math.toRadians(lon1);

		double dlat = Math.sin((lat1r - lat0r) / 2.0);
		double dlon = Math.sin((lon1r - lon0r) / 2.0);

		return Math.toDegrees(Math.asin(Math.sqrt(dlat * dlat + Math.cos(lat1r) * Math.cos(lat0r) * dlon * dlon)));
	}

	public static double getBearing(WorldPoint pt0, WorldPoint pt1, double distance) {

		double lat0 = Math.toRadians(pt0.lat);
		double lat1 = Math.toRadians(pt1.lat);
		double dlon = Math.toRadians(Math.abs(pt0.lon - pt1.lon));
		double dr = Math.toRadians(distance);
		double b = (Math.sin(lat1) - Math.cos(dr) * Math.sin(lat0)) / (Math.cos(lat0) * Math.sin(dr));
		double bearing = Math.toDegrees(Math.acos(b));
		if (Math.sin(dlon) < 0.0) {
			bearing = 360.0 - bearing;
		}
		return bearing;
	}

	public static WorldPoint getPosition(WorldPoint pt0, double bearing, double distance) {
		final double LIMIT = 0.00000000001;

		WorldPoint pt1 = new WorldPoint(pt0.lat, pt0.lon);

		double br = Math.toRadians(bearing);
		double dr = Math.toRadians(distance);
		double lat0 = Math.toRadians(pt0.lat);
		double lon0 = Math.toRadians(pt0.lon);

		if ((Math.abs(Math.cos(lat0)) < LIMIT) && !(Math.abs(Math.sin(br)) < LIMIT)) {
			return pt1;
		}

		double lon = 0.0;
		double lat = Math.asin(Math.sin(lat0) * Math.cos(dr) + Math.cos(lat0) * Math.sin(dr) * Math.cos(br));
		if (Math.abs(Math.cos(lat)) > LIMIT) {
			double dlon = Math.atan2(Math.sin(br) * Math.sin(dr) * Math.cos(lat0), Math.cos(dr) - Math.sin(lat0) * Math.sin(lat));
			lon = (lon0 - dlon + Math.PI) % (2 * Math.PI) - Math.PI;
		}
		pt1.lat = Math.toDegrees(lat);
		pt1.lon = Math.toDegrees(lon);
		return pt1;
	}

	public List getGreateCircle() {
		final int nsteps = 20;

		List list = new ArrayList();

		double distance = getDistance(pt0, pt1);
		double bearing = getBearing(pt0, pt1, distance);

		list.add(pt0);

		double dstep = distance / nsteps;
		double ddistance = dstep;
		while (ddistance < distance) {
			list.add(getPosition(pt0, bearing, ddistance));
			ddistance = ddistance + dstep;
		}
		list.add(pt1);

		return list;
	}

	public void printSVG(PrintWriter out, double resolution) {
		List list = getGreateCircle();
		city0.printSVG(out, resolution);
		city1.printSVG(out, resolution);

		out.print("<polyline style=&connect; points='\n");
		for (int j = 0; j < list.size(); j++) {
			if ((j % WorldMain.ptsPerLine) == 0) {
				out.print("\n\t");
			}
			WorldPoint pt = (WorldPoint) list.get(j);
			out.print(" " + pt.lon + "," + (90 - pt.lat));
		}
		out.print("' />\n");
	}
}

// ---------------------------------------------------------------------------------------------
// Coastline Segment
// ---------------------------------------------------------------------------------------------

class CoastlineSegment extends Segment {

	public int segment = 0;

	public int rank = 0;

	public int npts = 0;

	public List pts = null;

	public WorldRectangle extents = null;

	public CoastlineSegment(int segment) {
		pts = new ArrayList();
		this.segment = segment;
		this.kind = WATER;
	}

	public CoastlineSegment(CoastlineSegment seg, int npts) {
		this.segment = seg.segment; // TODO add to next segment
		this.rank = seg.rank;
		this.kind = seg.kind;
		this.npts = npts;
		this.extents = null;
		pts = new ArrayList(npts);
	}

	public CoastlineSegment(String str, String kind) {
		this.extents = null;
		this.kind = kind;

		try {
			int iseg = str.indexOf("segment");
			int irank = str.indexOf("rank");
			int ipoints = str.indexOf("points");

			this.segment = toInt(str.substring(iseg + 7, irank));
			this.rank = toInt(str.substring(irank + 4, ipoints));
			this.npts = toInt(str.substring(ipoints + 6));

			pts = new ArrayList(npts);

		} catch (Exception e) {
			System.err.println("ERROR: Segment: " + e + ": " + str);
			e.printStackTrace(System.err);
		}
	}

	public List getPoints() {
		return pts;
	}

	public WorldPoint getFirstPoint() {
		return (WorldPoint) pts.get(0);
	}

	public WorldPoint getLastPoint() {
		return (WorldPoint) pts.get(pts.size() - 1);
	}

	public static boolean isSegment(String str) {
		return str.startsWith("segment");
	}

	public static boolean isPoint(String str) {
		return str.startsWith("\t");
	}

	public void add(WorldPoint pt) {
		npts = npts + 1;
		this.extents = null;
		pts.add(new WorldPoint(pt.lat, pt.lon));
	}

	public void add(CoastlineSegment seg) {
		npts = npts + seg.npts;
		pts.addAll(seg.getPoints());
		this.extents = null;
	}

	public void add(CoastlineSegment seg, int firstIndex) {
		List list = seg.getPoints();
		for (int i = firstIndex; i < list.size(); i++) {
			WorldPoint pt = (WorldPoint) list.get(i);
			add(pt);
		}
	}

	public void add(List list) {
		npts = npts + list.size();
		pts.addAll(list);
		this.extents = null;
	}

	public void resetExtents() {
		this.extents = null;
	}

	public void reverse() {
		int imid = (pts.size() - (pts.size() % 2)) / 2;
		int iend = pts.size();
		for (int ibeg = 0; ibeg < imid; ibeg++) {
			iend = iend - 1;
			WorldPoint pt = (WorldPoint) pts.get(ibeg);
			pts.set(ibeg, pts.get(iend));
			pts.set(iend, pt);
		}
	}

	public boolean intersects(CoastlineSegment seg) {
		WorldRectangle rectA = getExtents();
		WorldRectangle rectB = seg.getExtents();

		return rectA.intersects(rectB);
	}

	public WorldRectangle getExtents() {
		if (extents != null) {
			return extents;
		}
		extents = new WorldRectangle(0.0, 0.0, 0.0, 0.0);
		if (pts == null || pts.size() == 0) {
			return extents;
		}
		WorldPoint pt = (WorldPoint) pts.get(0);
		extents.minlat = pt.lat;
		extents.maxlat = pt.lat;
		extents.minlon = pt.lon;
		extents.maxlon = pt.lon;
		for (int i = 1; i < pts.size(); i++) {
			extents.add((WorldPoint) pts.get(i));
		}
		return extents;
	}

	public boolean isSmaller(double dlat, double dlon) {
		if ((extents.maxlat - extents.minlat) > dlat)
			return false;

		if ((extents.maxlon - extents.minlon) > dlon)
			return false;

		return true;
	}

	public boolean isConnected() {
		final double resolution = 0.0000001;
		return isConnected(resolution);
	}

	public boolean isConnected(double resolution) {
		if (pts.size() < 2)
			return true;

		WorldPoint pt1 = (WorldPoint) pts.get(0);
		WorldPoint pt2 = (WorldPoint) pts.get(pts.size() - 1);

		return (pt1.isSame(pt2, resolution));
	}

	public static int find(WorldPoint pt, List pts, double resolution) {
		if (pts == null || pts.size() == 0)
			return -1;
		for (int i = 0; i < pts.size(); i++) {
			WorldPoint pt0 = (WorldPoint) pts.get(i);
			if (pt0.isSame(pt, resolution)) {
				return i;
			}
		}
		return -1;
	}

	public static int numberOfSamePoints(List pts1, List pts2, double resolution) {
		int counter = 0;
		if (pts1 == null || pts1.size() <= 0)
			return counter;
		if (pts2 == null || pts2.size() <= 0)
			return counter;

		for (int i = 0; i < pts1.size(); i++) {
			WorldPoint pt1 = (WorldPoint) pts1.get(i);
			for (int j = 0; j < pts2.size(); j++) {
				WorldPoint pt2 = (WorldPoint) pts2.get(j);
				if (pt1.isSame(pt2, resolution)) {
					counter++;
				}
			}
		}
		return counter;
	}

	public static int matchHeadToHead(List pts1, List pts2, double resolution) {
		if (pts1 == null || pts1.size() <= 0)
			return -1;

		if (pts2 == null || pts2.size() <= 0)
			return -1;

		int i1 = 0;
		WorldPoint pt = (WorldPoint) pts1.get(i1);
		int i0 = find(pt, pts2, resolution);
		if (i0 == -1)
			return -1;

		for (int j0 = i0; j0 >= 0; j0--) {
			if (i1 >= pts1.size())
				return -1;
			WorldPoint pt2 = (WorldPoint) pts2.get(j0);
			WorldPoint pt1 = (WorldPoint) pts1.get(i1++);
			if (pt1.isSame(pt2, resolution) == false) {
				return -1;
			}
		}
		return i0;
	}

	public static int matchHeadToTail(List pts1, List pts2, double resolution) {
		if (pts1 == null || pts1.size() <= 0)
			return -1;
		if (pts2 == null || pts2.size() <= 0)
			return -1;

		int i1 = pts1.size() - 1;
		WorldPoint pt = (WorldPoint) pts1.get(i1);
		int i0 = find(pt, pts2, resolution);
		if (i0 == -1)
			return -1;

		for (int j0 = i0; j0 >= 0; j0--) {
			if (i1 < 0)
				return -1;
			WorldPoint pt2 = (WorldPoint) pts2.get(j0);
			WorldPoint pt1 = (WorldPoint) pts1.get(i1--);
			if (pt2.isSame(pt1, resolution) == false) {
				return -1;
			}
		}
		return i0;
	}

	public static int matchTailToHead(List pts1, List pts2, double resolution) {
		if (pts1 == null || pts1.size() <= 0)
			return -1;
		if (pts2 == null || pts2.size() <= 0)
			return -1;

		int i1 = 0;
		WorldPoint pt = (WorldPoint) pts1.get(i1);
		int i0 = find(pt, pts2, resolution);
		if (i0 == -1)
			return -1;

		for (int j0 = i0; j0 < pts2.size(); j0++) {
			if (i1 >= pts1.size())
				return -1;

			WorldPoint pt2 = (WorldPoint) pts2.get(j0);
			WorldPoint pt1 = (WorldPoint) pts1.get(i1++);
			if (pt2.isSame(pt1, resolution) == false) {
				return -1;
			}
		}
		return i0;
	}

	public static int matchTailToTail(List pts1, List pts2, double resolution) {
		if (pts1 == null || pts1.size() <= 0)
			return -1;
		if (pts2 == null || pts2.size() <= 0)
			return -1;

		int i1 = pts1.size() - 1;
		WorldPoint pt = (WorldPoint) pts1.get(i1);
		int i0 = find(pt, pts2, resolution);
		if (i0 == -1)
			return -1;

		for (int j0 = i0; j0 < pts2.size(); j0++) {
			if (i1 < 0)
				return -1;
			WorldPoint pt2 = (WorldPoint) pts2.get(j0);
			WorldPoint pt1 = (WorldPoint) pts1.get(i1--);
			if (pt2.isSame(pt1, resolution) == false) {
				return -1;
			}
		}
		return i0;
	}

	public void addPoint(String str) {
		extents = null;
		try {
			int imid = str.indexOf(' ');
			double lat = toDouble(str.substring(1, imid));
			double lon = toDouble(str.substring(imid + 1));
			if (lat == 0.0 || lon == 0.0) {
				System.err.println("WARNING: Zero lat/lon found: " + str);
			}
			pts.add(new WorldPoint(lat, lon));
		} catch (Exception e) {
			System.err.println("ERROR: WorldPoint: " + e + ": " + str);
			e.printStackTrace(System.err);
		}
	}

	public void printSVG(PrintWriter out, double resolution) {

		WorldRectangle extents = getExtents();
		if (isSmaller(resolution, resolution) == true)
			return;

		List list = getPoints();
		if (list.size() <= 1)
			return;

		boolean connected = isConnected();

		out.print("<metadata>\n");
		out.print("       <attr name='name' value='" + segment + "' />\n");
		out.print("       <attr name='id' value='" + segment + "' />\n");
		out.print("       <attr name='kind' value='" + kind + "' />\n");
		out.print("       <attr name='connected' value='" + connected + "' />\n");
		out.print("       <attr name='length' value='" + list.size() + "' />\n");
		out.print("       <attr name='min-lat' value='" + extents.minlat + "' />\n");
		out.print("       <attr name='max-lat' value='" + extents.maxlat + "' />\n");
		out.print("       <attr name='min-lon' value='" + extents.minlon + "' />\n");
		out.print("       <attr name='max-lon' value='" + extents.maxlon + "' />\n");
		out.print("</metadata>\n");

		out.print("<polyline style=\"" + getStyle() + "\" points='");

		int ptCounter = 0;
		WorldPoint prevPt = null;
		for (int j = 0; j < list.size(); j++) {
			WorldPoint pt = (WorldPoint) list.get(j);
			if (!pt.isSame(prevPt, resolution) || j == 0 || j == (list.size() - 1)) {
				prevPt = pt;
				ptCounter++;
				out.print(" " + decimal.format(pt.lon) + "," + decimal.format(90.0 - pt.lat));
				if ((ptCounter % WorldMain.ptsPerLine) == 0) {
					out.print("\n\t");
				}
			}
		}
		out.print("' />\n");
	}

	public void setRank(int value) {
		this.rank = value;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return this.kind;
	}
}

class Statistics {
	private static DecimalFormat decimal = new DecimalFormat();

	private static long _nsegments = 0;

	private static long _csegments = 0;

	private static long _ucsegments = 0;

	private String filename = null;

	public Statistics(String filename) {
		this.filename = filename;
		decimal.setMaximumFractionDigits(6);
		decimal.setMinimumFractionDigits(2);
	}

	public void printStatistics(List list, double resolution) {
		int connected = 0;
		for (int i = 0; i < list.size(); i++) {
			CoastlineSegment seg = (CoastlineSegment) list.get(i);

			WorldPoint ptA = seg.getFirstPoint();
			WorldPoint ptZ = seg.getLastPoint();

			if (seg.isConnected(resolution) == true) {
				connected++;
				continue;
			}

			if (seg.segment == 1874) {
				System.out.println("SEGMENT 1874: " + ptA);
				System.out.println("SEGMENT 1874: " + ptZ);
			}
			System.out.println(seg.segment + ": ptA = " + ptA + ": ptZ = " + ptZ + ", " + seg.npts);
		}
		int unconnected = list.size() - connected;
		System.out.println("NSEGMENTS: " + list.size() + ", connected=" + connected + ", unconnected=" + unconnected);

		_nsegments = _nsegments + list.size();
		_csegments = _csegments + connected;
		_ucsegments = _ucsegments + unconnected;
	}

	public static void printTotals() {
		double percent = 100.0 * _csegments / _nsegments;
		System.out.println("NSEGMENTS: " + _nsegments + ", connected=" + _csegments + ", unconnected=" + _ucsegments);
		System.out.println("PERCENT CONNECTED: " + percent);
	}
}

interface PrinterInterface {
	public void printHeader();

	public void printBody(List list);

	public void printTrailer();
}

class SVGPrinter implements PrinterInterface {

	double _resolution = 0.1;

	PrintWriter out = null;

	public SVGPrinter(double resolution, String filename) {
		_resolution = resolution;
		final boolean APPEND = true;
		try {
			out = new PrintWriter(new FileWriter(filename, APPEND));
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + e);
			e.printStackTrace(System.err);
		}
	}

	public void printHeader() {
		double x = -180.0;
		double y = 0;
		double width = 360.0;
		double height = 180.0;

		out.print("<?xml version=\"1.0\" standalone=\"no\"?>\n");
		// out.print("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" ");
		// out.print("\"http://www.w3c.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
		out.println("<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.0//EN' 'http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd' [");
		out.println("<!ENTITY style-land    \"fill:#1B9531;stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-water   \"fill:#17A6C8;stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-ice     \"fill:#ffffff;stroke:#ffffff;stroke-width:0.1;\">");
		out.println("<!ENTITY style-border  \"fill:none;   stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-text    \"fill:#aaaaaa;stroke:#000000;stroke-width:0.1;\">");
		out.println("<!ENTITY style-connect \"fill:none;   stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-river   \"fill:none;   stroke:#17A6C8;stroke-width:0.1;\">");
		out.println("<!ENTITY style-lake    \"fill:none;   stroke:#17A6C8;stroke-width:0.1;\">");
		out.println("<!ENTITY style-default \"fill:none;   stroke:#000000;stroke-width:0.1;\">");
		out.println("<!ENTITY style-city    \"font-family='Verdana' font-size='8'\">");
		out.println("<!ENTITY style-label   \"font-family='Verdana' font-size='8';fill:#FFFFFF;stroke:#000000;stroke-width:0.1;\">");
		out.println("]>");

		out.print("<!--\n");
		out.print("This file contains points (lats-lons) for the world map.\n");
		out.print("Athor: TomNevin@pacbell.net\n");
		out.print("Date:  09/18/2004  - \n");
		out.print("-->\n");
		out.print("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
		out.print("     width='100%'\n");
		out.print("     height='100%' \n");
		out.print("     viewBox='" + x + " " + y + " " + width + " " + height + "'\n");
		out.print("     preserveAspectRatio='xMidYMin'\n");
		out.print("     onload='init()'\n");
		out.print(" 	version='1.1'>\n");
		out.print("<rect style=\"&style-water;\" x='" + x + "' y='" + y + "' width='" + width + "' height='" + height + "' />");
		out.print("<script type='text/ecmascript'><![CDATA[\n");
		out.print("function init() {\n");
		out.print("	//	setInterval('updateTime();', 1000);\n");
		out.print("}\n");
		out.print("\n");
		out.print("function updateTime() {\n");
		out.print("}\n");
		out.print("function pan(evt, dx, dy) {\n");
		out.print("	var svgdoc = evt.getTarget().getOwnerDocument();\n");
		out.print("	var svgtag = svgdoc.getDocumentElement();\n");
		out.print("	var viewbox = svgtag.getAttribute('viewBox');\n");
		out.print("	var coords = viewbox.split(/ /);\n");
		out.print("	coords[0] = coords[0] + scale * dx;\n");
		out.print("	coords[1] = coords[1] + scale * dy;\n");
		out.print("	svgtag.setAttribute('viewBox', coords.join(' '));\n");
		out.print("}\n");
		out.print("	function zoom(evt, factor) {\n");
		out.print("		var svgdoc = evt.getTarget().getOwnerDocument();\n");
		out.print("		var svgtag = svgdoc.getDocumentElement();\n");
		out.print("		var viewbox = svgtag.getAttribute('viewBox');\n");
		out.print("		var coords = viewbox.split(/ /);\n");
		out.print("		coords[2] = coords[2] * factor;\n");
		out.print("		coords[3] = coords[3] * factor;\n");
		out.print("		svgtag.setAttribute('viewBox', coords.join(' '));\n");
		out.print("	}\n");
		out.print("	]]></script>\n");
		out.print("	<text x='25%' y='5%' fill='white' onclick='zoom(evt, 0.5)'>Zoom+</text>\n");
		out.print("	<text x='35%' y='5%' fill='white' onclick='zoom(evt, 2.0)'>Zoom-</text>\n");
		out.print("	<text x='45%' y='5%' fill='white' onclick='pan(evt, 0.0, -25.0)'>PanUp</text>\n");
		out.print("	<text x='55%' y='5%' fill='white' onclick='pan(evt, 0.0, 25.0)'>PanDown</text>\n");
		out.print("	<text x='65%' y='5%' fill='white' onclick='pan(evt, -25.0, 0.0)'>PanLeft</text>\n");
		out.print("	<text x='75%' y='5%' fill='white' onclick='pan(evt, 25.0, 0.0)'>PanRight</text>\n");
	}

	public void printBody(List list) {
		for (int i = 0; i < list.size(); i++) {
			Segment seg = (Segment) list.get(i);
			seg.printSVG(out, _resolution);
		}
	}

	public void printTrailer() {
		out.print("</svg>\n");
		out.close();
	}
}

class XmlPrinter implements PrinterInterface {

	double _resolution = 0.1;

	PrintWriter out = null;

	public XmlPrinter(double resolution, String filename) {
		_resolution = resolution;
		final boolean APPEND = true;
		try {
			out = new PrintWriter(new FileWriter(filename, APPEND));
		} catch (Exception e) {
			System.err.println("ERROR:  processing file => " + filename + "," + e);
			e.printStackTrace(System.err);
		}
	}

	public void printHeader() {
		double x = -180.0;
		double y = 0;
		double width = 360.0;
		double height = 180.0;

		out.print("<?xml version=\"1.0\" standalone=\"no\"?>\n");
		// out.print("<!DOCTYPE doc PUBLIC \"-//W3C//DTD SVG 1.1//EN\" ");
		// out.print("\"http://www.w3c.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
		out.println("<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.0//EN' 'http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd' [");
		out.println("<!ENTITY style-land    \"fill:#1B9531;stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-water   \"fill:#17A6C8;stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-ice     \"fill:#ffffff;stroke:#ffffff;stroke-width:0.1;\">");
		out.println("<!ENTITY style-border  \"fill:none;   stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-text    \"fill:#aaaaaa;stroke:#000000;stroke-width:0.1;\">");
		out.println("<!ENTITY style-connect \"fill:none;   stroke:#111111;stroke-width:0.1;\">");
		out.println("<!ENTITY style-river   \"fill:none;   stroke:#17A6C8;stroke-width:0.1;\">");
		out.println("<!ENTITY style-lake    \"fill:none;   stroke:#17A6C8;stroke-width:0.1;\">");
		out.println("<!ENTITY style-default \"fill:none;   stroke:#000000;stroke-width:0.1;\">");
		out.println("<!ENTITY style-city    \"font-family='Verdana' font-size='8'\">");
		out.println("<!ENTITY style-label   \"font-family='Verdana' font-size='8';fill:#FFFFFF;stroke:#000000;stroke-width:0.1;\">");
		out.println("]>");
		out.print("<!--\n");
		out.print("This file contains points (lats-lons) for the world map.\n");
		out.print("Athor: TomNevin@pacbell.net\n");
		out.print("Date:  09/18/2004  - \n");
		out.print("-->\n");
		out.print("<svg xmlns=\"http://www.w3.org/2000/svg\"\n");
		out.print("     width='100%'\n");
		out.print("     height='100%' \n");
		out.print("     viewBox='" + x + " " + y + " " + width + " " + height + "'\n");
		out.print("     preserveAspectRatio='xMidYMin'\n");
		out.print("     onload='init()'\n");
		out.print(" 	version='1.1'>\n");
		out.print("<rect style=\"&style-water;\" x='" + x + "' y='" + y + "' width='" + width + "' height='" + height + "' />");
		out.print("	<text x='25%' y='5%' fill='white' onclick='zoom(evt, 0.5)'>Zoom+</text>\n");
		out.print("	<text x='35%' y='5%' fill='white' onclick='zoom(evt, 2.0)'>Zoom-</text>\n");
		out.print("	<text x='45%' y='5%' fill='white' onclick='pan(evt, 0.0, -25.0)'>PanUp</text>\n");
		out.print("	<text x='55%' y='5%' fill='white' onclick='pan(evt, 0.0, 25.0)'>PanDown</text>\n");
		out.print("	<text x='65%' y='5%' fill='white' onclick='pan(evt, -25.0, 0.0)'>PanLeft</text>\n");
		out.print("	<text x='75%' y='5%' fill='white' onclick='pan(evt, 25.0, 0.0)'>PanRight</text>\n");
	}

	public void printBody(List list) {
		for (int i = 0; i < list.size(); i++) {
			Segment seg = (Segment) list.get(i);
			// seg.printXml(out, _resolution);
		}
	}

	public void printTrailer() {
		out.print("</doc>\n");
		out.close();
	}
}
