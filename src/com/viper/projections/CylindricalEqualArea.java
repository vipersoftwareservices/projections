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
 * -----------------------------------------------------------------------------
 */

package com.viper.projections;

public class CylindricalEqualArea extends MapProjection {

    public CylindricalEqualArea() {
    }

    /**
     * 
     * @param B
     * @param e
     * @return
     */
    public double computeT(double B, double e) {

        double e2 = e * e;
        double e4 = e2 * e2;
        double e6 = e4 * e2;

        return B + (e2 / 3.0 + 31.0 * e4 / 180.0 + 517.0 * e6 / 5040.0) * sin(2.0 * B)
                + (23.0 * e4 / 360.0 + 251.0 * e6 / 3780.0) * sin(4.0 * B) + (761.0 * e6 / 45360.0) * sin(6.0 * B);
    }

    /**
     * 
     * @param Y
     * @param E
     * @return
     */
    public double computeQ(double Y, double E) {

        double SY = Math.sin(Y);

        if (E == 0.0) { // Just a guess
            return SY;
        }

        double E2 = E * E;

        return (1.0 - E2) * (SY / (1.0 - E2 * SY * SY) - (1.0 / (2.0 * E)) * Math.log((1.0 - E * SY) / (1.0 + E * SY)));
    }

    /**
     * 
     * @param lat
     * @param E
     * @return
     */
    public double computeK0(double lat, double E) {

        double slat = sin(lat);

        return cos(lat) / sqrt(1.0 - E * E * slat * slat);

    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        if (getEccentricity() == 0.0) {
            toProjectionSphere(mp, pp);
        } else {
            toProjectionEllipsoid(mp, pp);
        }
    }

    /**
     * 
     * @param mp
     * @param pp
     */
    public void toProjectionSphere(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to radians.
        double lon = mp.lon * toRadians;
        double lat = mp.lat * toRadians;
        double lat1 = getParallel1() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();

        // o Formula 10-1.
        pp.lon = a * (lon - lon0) * cos(lat1);

        // o Formula 10-2.
        pp.lat = a * sin(lat) / cos(lat1);
    }

    /**
     * 
     * @param mp
     * @param pp
     */
    public void toProjectionEllipsoid(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to radians.
        double lon = mp.lon * toRadians;
        double lat = mp.lat * toRadians;
        double lat1 = getParallel1() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();
        double E = getEccentricity();

        // Formula 10-13
        double k0 = computeK0(lat1, E);

        // Formula 3-12
        double Q = computeQ(lat1, E);

        // o Formula 10-1.
        pp.lon = a * k0 * (lon - lon0);

        // o Formula 10-2.
        pp.lat = a * Q / (2.0 * k0);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {
        if (getEccentricity() == 0.0) {
            toLatLonSphere(pp, mp);
        } else {
            toLatLonEllipsoid(pp, mp);
        }
    }

    /**
     * 
     * @param pp
     * @param mp
     */
    public void toLatLonSphere(MapPoint pp, MapPoint mp) {

        double lon = pp.lon;
        double lat = pp.lat;
        double lat1 = getParallel1() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();

        // o Formula 10-6
        double Lat = asin((lat / a) * cos(lat1));

        // o Formula 10-7
        double Lon = (lon / (a * cos(lat1))) + lon0;

        mp.lat = Lat * toDegrees;
        mp.lon = Lon * toDegrees;
    }

    /**
     * 
     * @param pp
     * @param mp
     */
    public void toLatLonEllipsoid(MapPoint pp, MapPoint mp) {

        double lon = pp.lon;
        double lat = pp.lat;
        double lat1 = getParallel1() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();
        double E = getEccentricity();

        // Formula 10-13
        double k0 = computeK0(lat1, E);

        // Formula 3-12
        double qp = computeQ(lat1, E);

        // Formula 10-26
        double B = asin(2.0 * lat * k0 / (a * qp));

        // Formula 10-27
        double Lon = lon0 + lon / (a * k0);

        // Formula 3-18
        double Lat = computeT(B, E);

        mp.lon = Lon * toDegrees;
        mp.lat = Lat * toDegrees;
    }
}
