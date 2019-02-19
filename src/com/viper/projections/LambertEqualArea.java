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

public class LambertEqualArea extends MapProjection {

    public LambertEqualArea() {
    }

    /**
     * 
     * @param lat
     * @param E
     * @return
     */ 
    public double computeQ(double lat, double E) {

        double sinLat = sin(lat);
        double E2 = E * E;

        return (1.0 - E2)
                * (sinLat / (1.0 - E2 * sinLat * sinLat) - (1.0 / (2.0 * E)) * log((1.0 - E * sinLat) / (1.0 + E * sinLat)));
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

        // o Convert Degrees to Radians
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        // o Formula 24-2
        double k = sqrt(2.0 / (1.0 + sin(lat1) * sin(lat) + cos(lat1) * cos(lat) * cos(lon - lon0)));

        // o Formula 24-17
        pp.lon = a * k * cos(lat) * sin(lon - lon0);

        // o Formula 24-18
        pp.lat = a * k * (cos(lat1) * sin(lat) - sin(lat1) * cos(lat) * cos(lon - lon0));

    }

    /**
     * 
     * @param mp
     * @param pp
     */
    public void toProjectionEllipsoid(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to Radians
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double E = getEccentricity();
        double a = getRadius();

        double E2 = E * E;
        double sinLat = sin(lat);
        double sinLat1 = sin(lat1);

        // o Formula 14-15
        double m = cos(lat) / sqrt(1.0 - E2 * sinLat * sinLat);
        double m1 = cos(lat1) / sqrt(1.0 - E2 * sinLat1 * sinLat1);

        // o Formula 3-12
        double q = computeQ(lat, E);
        double q1 = computeQ(lat1, E);
        double qp = computeQ(lat0, E);

        // o Formula 3-11
        double beta = asin(q / qp);
        double beta1 = asin(q1 / qp);

        // o Formula 3-13
        double Rq = a * sqrt(qp / 2.0);

        // o Formula 24-20
        double D = a * m1 / (Rq * cos(beta1));

        // o Formula 24-19
        double B = Rq * sqrt(2.0 / (1.0 + sin(beta1) * sin(beta) + cos(beta1) * cos(beta) * cos(lon - lon0)));

        // o Formula 24-17
        pp.lon = B * D * cos(beta) * sin(lon - lon0);

        // o Formula 24-18
        pp.lat = (B / D) * (cos(beta1) * sin(beta) - sin(beta1) * cos(beta) * cos(lon - lon0));

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

        // o Convert to Radians
        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        // o Formula 20-18
        double P = sqrt(lat * lat + lon * lon);
        if (P == 0) {
            mp.lat = lat1 * toDegrees;
            mp.lon = lon0 * toDegrees;
            return;
        }

        // o Formula 20-19
        double C = 2.0 * asin(P / (2.0 * a));

        // o Formula 20-14
        double Lat = asin(cos(C) * sin(lat1) + (lat * sin(C) * cos(lat1) / P));

        // o Formula 20-15
        double Lon = 0.0;
        double den = 0.0;
        double num = 0.0;

        if (getParallel1() == 90.0) {
            den = -lat;
            num = lon;

        } else if (getParallel1() == -90.0) {
            den = lat;
            num = lon;

        } else {
            num = lon * sin(C);
            den = P * cos(lat1) * cos(C) - lat * sin(lat1) * sin(C);
        }

        Lon = lon0 + atan(num / den);
        if (den < 0.0) {
            Lon = Lon + PI;
        }

        mp.lat = Lat * toDegrees;
        mp.lon = Lon * toDegrees;

    }

    /**
     * 
     * @param pp
     * @param mp
     */
    public void toLatLonEllipsoid(MapPoint pp, MapPoint mp) {

        // o Convert to Radians
        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();
        double E = getEccentricity();
        double E2 = E * E;
        double sinLat1 = sin(lat1);

        // o Formula 3-12
        double q1 = computeQ(lat1, E);
        double qp = computeQ(lat0, E);

        // o Formula 3-11
        double beta1 = asin(q1 / qp);

        // o Formula 3-13
        double Rq = a * sqrt(qp / 2.0);

        // o Formula 14-15
        double m1 = cos(lat1) / sqrt(1.0 - E2 * sinLat1 * sinLat1);

        // o Formula 24-20
        double D = a * m1 / (Rq * cos(beta1));

        // o Formula 24-28
        double P = sqrt(pow(lon / D, 2.0) + pow(D * lat, 2.0));

        // o Formula 24-29
        double Ce = 2.0 * asin(P / (2.0 * Rq));

        // o Formula 24-30
        double beta = asin(cos(Ce) * sin(beta1) + (D * lat * sin(Ce) * cos(beta1) / P));

        // o Formula 3-18
        double Lat = computeT(beta, E);

        mp.lat = Lat * toDegrees;

        // o Formula 24-27
        double q = qp * (cos(Ce) * sin(beta1) + (D * lat * sin(Ce) * cos(beta1) / P));

        // o Formula 24-26
        double Lon = lon0 + atan(lon * sin(Ce) / (D * P * cos(beta1) * cos(Ce) - D * D * lat * sin(beta1) * sin(Ce)));

        mp.lon = Lon * toDegrees;
    }
}
