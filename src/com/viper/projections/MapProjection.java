/*
 * -----------------------------------------------------------------------------
 *                      VIPER SOFTWARE SERVICES
 * -----------------------------------------------------------------------------
 *
 * MIT License
 * 
 * Copyright (c) #{classname}.html #{util.YYYY()} Viper Software Services
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 * 
 * @version 1.0, 06/15/2003, Original version
 *
 * @note 
 *    This package was developed from the following references:
 *
 *      Reference 1
 * 		Map Projections - A Working Manual,
 *		USGS Professional Paper, First Ed., 1987.
 *		Hereafter this reference will be refered to as the USGS Manual.
 *
 *      Reference 2
 *		Defense Mapping Agency Product Specifications for
 *		ARC Digitized Raster Graphics (ADRG)
 *		First Edition July 1988, Coordination Draft.
 *
 *	Reference 3
 *		An Album of Map Projections
 *		US Geological Survey
 * 		Professional Paper 1453
 *  
 * -----------------------------------------------------------------------------
 */

package com.viper.projections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MapProjection {

    public final double PI = Math.PI; 
    public final double toRadians = Math.PI / 180.0; 
    public final double toDegrees = 180.0 / Math.PI; 
    public final double INFINITY = 3.0E25; // SUN 386i // TBD

    // Conversion of meters to Nautical Miles 
    public final double MetersToNauticalMiles = 0.00053999568; 
    public final double NauticalMilesToMeters = 1852.0;

    // Ellipsoid Data
    //
    // Use the radius to convert the output to the desired units.
    // The Equatorial radius should be used for ellipsoid calculations
    // The Standard Radius should be use for spheriod calculations.
    //
    // Note: All radius values are in meters, unless noted otherwise.
    //
    // Eccentricity of 0.0 => spherical calculations
    // Eccentricity of Earth => Ellipsoid calculations
    //
    // Axes in Meters
    // Code Spheroid Name Semi-Major Semi-Minor
    //
    // 0 Clarke 1866 (default) 6378206.4 6356583.8
    // 1 Clarke 1880 6378249.145 6356514.86955
    // 2 Bessel 6377397.155 6356078.9628
    // 3 International 1967 6378157.5 6356772.2
    // 4 International 1909 6378388.0 6356911.94613
    // 5 WGS 72 6378135.0 6356750.519915
    // 6 Everest 6377276.3452 6356075.4133
    // 7 WGS 66 6378145.0 6356759.769356
    // 8 GRS 1980 6378137.0 6356752.31414
    // 9 Airy 6377563.396 6356256.91
    // 10 Modified Everest 6377304.063 6356103.039
    // 11 Modified Airy 6377340.189 6356036.143
    // 12 WGS 84 6378137.0 6356752.314245
    // 13 Southeast Asia 6378155.0 6356773.3205
    // 14 Australian National 6378160.0 6356774.719
    // 15 Krassovsky 6378245.0 6356863.0188
    // 16 Hough 6378270.0 6356794.343479
    // 17 Mercury 1960 6378166.0 6356784.283666
    // 18 Modified Mercury 1968 6378150.0 6356768.337303
    // 19 Sphere 6370997.0 6370997.0
    //
    // International Ellipsoid
    public final double InternationalEccentricity = 0.08199188984; 
    public final double InternationalRadius = 6378388.0;

    // WGS84 Ellipsoid
    public final double WGS84_Eccentricity = 0.0818191908426; 
    public final double WGS84_MajorRadius = 6378137.0; 
    public final double WGS84_MinorRadius = 6356752.3142;

    // GRS80/NAD83
    public final double GRS80_Eccentricity = 0.081819191043; 
    public final double GRS80_MajorRadius = 6378137.0; 
    public final double GRS80_MinorRadius = 6356752.314;

    // WGS66 Ellipsoid
    public final double WGS66_Eccentricity = 0.0; // TBD 
    public final double WGS66_MajorRadius = 6378145.0;

    // GRS67/IAU68 Ellipsoid
    public final double GRS67_Eccentricity = 0.0; // TBD 
    public final double GRS67_MajorRadius = 6378160.0;

    public final double IAU68_Eccentricity = 0.0; // TBD

    public final double IAU68_MajorRadius = 6378160.0;

    // WGS72 Ellipsoid
    public final double WGS72_Eccentricity = 0.081818810663; // TBD

    public final double WGS72_MajorRadius = 6378135.0;

    public final double WGS72_MinorRadius = 6356750.520;

    // Krasovsky Ellipsoid
    public final double KRASO_Eccentricity = 0.0; // TBD

    public final double KRASO_MajorRadius = 6378245.0;

    // Clarke1866 Ellipsoid
    public final double CLA66_Eccentricity = 0.0; // TBD

    public final double CLA66_MajorRadius = 6378206.400;

    public final double CLA66_MinorRadius = 6356584.467;

    public final double NAD27_Eccentricity = 0.0; // TBD

    public final double NAD27_MajorRadius = 6378206.400;

    public final double NAD27_MinorRadius = 6356584.467;

    // Clarke1880 Ellipsoid
    public final double CLA80_Eccentricity = 0.0; // TBD

    public final double CLA80_MajorRadius = 6378249.145;

    public final double CLA80_MinorRadius = 6356514.967;

    // Suggested values
    public final double EarthEccentricity = 0.0822719;

    public final double StandardRadius = 6370997.0;

    public final double EquatorialRadius = CLA66_MajorRadius;

    // Projection Parameters

    double originLat = 0.0; 
    double originLon = 0.0; 
    double eccentricity = 0.0; 
    double radius = toDegrees; 
    double convergence = 0.0;

    // Standard Parallel #1, a latitude.
    double SP1 = 30.0;

    // Standard Parallel #2, a latitude.
    double SP2 = 45.0;

    // Transform toGlobal = new Transform(1.0, 1.0, 0.0, 0.0);
    // Transform toPixels = new Transform(1.0, 1.0, 0.0, 0.0);

    static Map<String, MapProjection> cache = new HashMap<String, MapProjection>();

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLon() {
        return originLon;
    }

    public void setOriginLon(double originLon) {
        this.originLon = originLon;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getParallel1() {
        return this.SP1;
    }

    public void setParallel1(double SP1) {
        this.SP1 = SP1;
    }

    public double getParallel2() {
        return this.SP2;
    }

    public void setParallel2(double SP2) {
        this.SP2 = SP2;
    }

    public static MapProjection getInstance(String name) {

        MapProjection projection = cache.get(name);
        if (projection != null) {
            return projection;
        }

        try {
            String classname = "com.viper.projections." + name;
            projection = (MapProjection) Class.forName(classname).newInstance();

            cache.put(name, projection);

        } catch (Exception e) {
            System.out.println("UKNOWN: " + name);
        }

        return projection;
    }

    public boolean inView(MapPoint mp) {
        return true;
    }

    public abstract void toProjection(MapPoint mp, MapPoint pp);

    public abstract void toLatLon(MapPoint pp, MapPoint mp);

    /**
     * 
     * @param ma
     * @param pa
     */
    public void toProjection(Grid ma, Grid pa) {

        MapPoint pt = new MapPoint();
        MapPoint pp = new MapPoint();

        pt.lat = ma.getNorth();
        pt.lon = ma.getEast();

        toProjection(pt, pp);

        pa.setNorth(pp.lat);
        pa.setEast(pp.lon);

        pt.lat = ma.getSouth();
        pt.lon = ma.getWest();

        toProjection(pt, pp);

        pa.setSouth(pp.lat);
        pa.setWest(pp.lon);
    }

    /**
     * 
     * @param pa
     * @param ma
     */
    public void toLatLon(Grid pa, Grid ma) {

        MapPoint pt = new MapPoint();
        MapPoint mp = new MapPoint();

        pt.lat = pa.getNorth();
        pt.lon = pa.getEast();

        toProjection(pt, mp);

        ma.setNorth(mp.lat);
        ma.setEast(mp.lon);

        pt.lat = pa.getSouth();
        pt.lon = pa.getWest();

        toProjection(pt, mp);

        ma.setSouth(mp.lat);
        ma.setWest(mp.lon);
    }

    /**
     * 
     * @param mpList
     * @param ppList
     */
    public void toProjection(List<MapPoint> mpList, List<MapPoint> ppList) {

        for (int i = 0; i < mpList.size(); i++) {
            MapPoint mp = mpList.get(i);
            MapPoint pp = ppList.get(i);

            toProjection(mp, pp);
        }
    }

    public void toLatLon(List<MapPoint> ppList, List<MapPoint> mpList) {

        for (int i = 0; i < ppList.size(); i++) {
            MapPoint mp = mpList.get(i);
            MapPoint pp = ppList.get(i);

            toLatLon(pp, mp);
        }
    }

    public double getAzimuth(MapPoint pt1, MapPoint pt2) {

        double S0 = (pt2.lon - pt1.lon) * toRadians;

        double S1 = sin(S0) * cos(pt2.lat * toRadians);

        double S2 = cos(pt1.lat * toRadians) * sin(pt2.lat * toRadians)
                - sin(pt1.lat * toRadians) * cos(pt2.lat * toRadians) * cos(S0);

        double azimuth = atan2(S1, S2) * toDegrees;

        return azimuth;
    }

    public double getDistance(MapPoint pt1, MapPoint pt2) {

        return getDistanceGreatCircle2(pt1, pt2);

    }

    public double getDistanceGreatCircle1(MapPoint pt1, MapPoint pt2) {

        double lat1 = pt1.lat * toRadians;
        double lat2 = pt2.lat * toRadians;
        double lon1 = pt1.lon * toRadians;
        double lon2 = pt2.lon * toRadians;

        double a = getRadius();

        return a * acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon1 - lon2));
    }

    public double getDistanceGreatCircle2(MapPoint pt1, MapPoint pt2) {

        double lat1 = pt1.lat * toRadians;
        double lat2 = pt2.lat * toRadians;
        double lon1 = pt1.lon * toRadians;
        double lon2 = pt2.lon * toRadians;

        double a = getRadius();

        double dlat = sin((lat1 - lat2) / 2.0);
        double dlon = sin((lon1 - lon2) / 2.0);

        return a * 2.0 * asin(sqrt(dlat * dlat + cos(lat1) * cos(lat2) * dlon * dlon));
    }

    public MapPoint toLatLon2(MapPoint pt1, double distance, double bearing) {

        return toLatLon2GeneralForm(pt1, distance, bearing);
    }

    public MapPoint toLatLon2GeneralForm(MapPoint pt1, double distance, double bearing) {

        MapPoint pt2 = new MapPoint();

        if (distance == 0.0) {
            pt2.lat = pt1.lat;
            pt2.lon = pt1.lon;
            return pt2;
        }

        double lat1 = pt1.lat * toRadians;
        double lon1 = pt1.lon * toRadians;
        double b = -bearing * toRadians;
        double d = distance / getRadius();

        double Lat = asin(sin(lat1) * cos(d) + cos(lat1) * sin(d) * cos(b));

        double y = sin(b) * sin(d) * cos(lat1);
        double x = cos(d) - sin(lat1) * sin(Lat);
        double dlon = atan2(y, x);

        double Lon = ((lon1 - dlon + PI) % (2.0 * PI)) - PI;

        pt2.lat = Lat * toDegrees;
        pt2.lon = Lon * toDegrees;

        return pt2;
    }

    /**
     * 
     * @param MajorRadius
     * @param MinorRadius
     * @return
     * 
     *         F = (MajorRadius - MinorRadius) / MajorRadius E = sqrt(2F-F*F).
     */
    public double computeEccentricity(double MajorRadius, double MinorRadius) {

        double F = (MajorRadius - MinorRadius) / MajorRadius;

        return sqrt(2 * F - F * F);
    }

    protected void println(String msg) {
        System.out.println(msg);
    }

    public double abs(double value) {
        return (value < 0.0) ? -value : value;
    }

    public double sign(double value) {
        return (value < 0.0) ? -1.0 : 1.0;
    }

    public double square(double value) {
        return value * value;
    }

    public double cot(double value) {

        double v = tan(value);
        if (v != 0.0) {
            v = 1.0 / v;
        }
        return v;
    }

    public double sin(double value) {
        return Math.sin(value);
    }

    public double cos(double value) {
        return Math.cos(value);
    }

    public double tan(double value) {
        return Math.tan(value);
    }

    public double asin(double value) {
        return Math.asin(value);
    }

    public double acos(double value) {
        return Math.acos(value);
    }

    public double atan(double value) {
        return Math.atan(value);
    }

    public double atan2(double y, double x) {
        return Math.atan2(y, x);
    }

    public double sqrt(double value) {
        return Math.sqrt(value);
    }

    public double pow(double value, double power) {
        return Math.pow(value, power);
    }

    public double exp(double value) {
        return Math.exp(value);
    }

    public double log(double value) {
        return Math.log(value);
    }

    public int toInt(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double toDouble(String str) {
        if (str == null || str.length() == 0) {
            return 0.0;
        }

        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double crossProduct(MapPoint pt1, MapPoint pt2, MapPoint pt3) {

        return (pt2.lon - pt1.lon) * (pt3.lat - pt2.lat) - (pt2.lat - pt1.lat) * (pt3.lon - pt2.lon);
    }

    public void computeRhumbLine(List<MapPoint> mpList, MapPoint pt1, MapPoint pt2, int npts) {

        mpList.clear();
        MapPoint mp = new MapPoint(pt1.lat, pt1.lon);

        double dLat = (pt2.lat - pt1.lat) / npts;
        double dLon = (pt2.lon - pt1.lon) / npts;

        for (int i = 0; i <= npts; i++) {
            MapPoint pp = new MapPoint();
            if (i == npts) {
                mp = pt2;
            }

            toProjection(mp, pp);

            mp.lat = mp.lat + dLat;
            mp.lon = mp.lon + dLon;

            mpList.add(pp);
        }
    }

    public void computeLine(List<MapPoint> mpList, MapPoint pt1, MapPoint pt2, int npts) {

        mpList.clear();
        MapPoint mp = new MapPoint(pt1.lat, pt1.lon);

        double dLat = (pt2.lat - pt1.lat) / npts;
        double dLon = (pt2.lon - pt1.lon) / npts;

        for (int i = 0; i < npts; i++) {
            MapPoint pp = new MapPoint();
            if (i == npts) {
                mp = pt2;
            }

            pp.lat = mp.lat;
            pp.lon = mp.lon;

            mp.lat = mp.lat + dLat;
            mp.lon = mp.lon + dLon;

            mpList.add(pp);
        }
    }

    public List<MapPoint> computeEllipse(MapPoint pcenter, double major, double minor, double angle, int npts) {

        MapPoint mp = new MapPoint();
        List<MapPoint> list = new ArrayList<MapPoint>();

        angle = abs(angle % 360.0);

        double x2 = major * Math.cos(angle * toRadians);
        double y2 = minor * Math.sin(angle * toRadians);

        double PI2 = 2.0 * PI;
        double arc = PI2 / npts;

        for (double rads = 0.0; rads < PI2; rads = rads + arc) {
            double x1 = major * Math.cos(rads);
            double y1 = minor * Math.sin(rads);

            mp.lat = -x1 * y2 + y1 * x2 + pcenter.lat;
            mp.lon = x1 * x2 + y1 * y2 + pcenter.lon;

            MapPoint pp = new MapPoint();

            toProjection(mp, pp);

            list.add(pp);
        }
        return list;
    }

    public List<MapPoint> computeArc(MapPoint pcenter, double radius, double angle1, double angle2, int npts) {

        MapPoint mp = new MapPoint(), pp;
        List<MapPoint> list = new ArrayList<MapPoint>();

        double a = getRadius();

        double rangle1 = angle1 * toRadians;
        double rangle2 = angle2 * toRadians;
        double arc = (rangle1 - rangle2) / npts;
        double ang = rangle1;

        for (int i = 1; i < npts; i++) {
            mp.lon = pcenter.lon + a * Math.cos(ang);
            mp.lat = pcenter.lat + a * Math.sin(ang);

            pp = new MapPoint();

            toProjection(mp, pp);

            list.add(pp);

            ang = ang + arc;
        }

        mp.lon = pcenter.lon + a * Math.cos(rangle2);
        mp.lat = pcenter.lat + a * Math.sin(rangle2);

        pp = new MapPoint();

        toProjection(mp, pp);

        list.add(pp);

        return list;
    }
}
