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

public class Bonne extends MapProjection {

    public Bonne() {
    }

    /**
     * 
     * @param lat
     * @param E
     * @return
     */
    public double computeM(double lat, double E) {

        double E2 = E * E;
        double E4 = E2 * E2;
        double E6 = E4 * E2;

        return (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0) * lat
                - (3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0) * sin(2.0 * lat)
                + (15.0 * E4 / 256.0 + 45.0 * E6 / 1024.0) * sin(4.0 * lat) - (35.0 * E6 / 3072.0) * sin(6.0 * lat);
    }

    /**
     * 
     * @param u
     * @param E
     * @return
     */
    public double computeT(double u, double E) {

        double E2 = E * E;
        double E3 = E * E2;
        double E4 = E * E3;

        return u + (3.0 * E / 2.0 - 27.0 * E3 / 32.0) * Math.sin(2.0 * u)
                + (21.0 * E2 / 16.0 - 55.0 * E4 / 32.0) * Math.sin(4.0 * u) + (151.0 * E3 / 96.0) * Math.sin(6.0 * u)
                + (1097.0 * E4 / 512.0) * Math.sin(8.0 * u);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Grab all parameters
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();
        double e = getEccentricity();

        // Formula 14-15, page 140
        double m = cos(lat) / sqrt(1.0 - pow(e * sin(lat), 2.0));
        double m1 = cos(lat1) / sqrt(1.0 - pow(e * sin(lat1), 2.0));

        // Formula 3-21, page 140
        double M = computeM(lat, e);
        double M1 = computeM(lat1, e);

        // o Formula 19-8
        double p = m1 / sin(lat1) + M1 - M;

        // o Formula 19-9
        double E = m * (lon - lon0) / p;

        // o Formula 19-3.
        pp.lon = a * p * sin(E);

        // o Formula 19-4.
        pp.lat = a * (m1 / sin(lat1) - p * cos(E));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        // o Grab all parameters
        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();
        double e = getEccentricity();
        double e2 = pow(e, 2.0);
        double e4 = pow(e, 4.0);
        double e6 = pow(e, 6.0);

        // Formula 14-15, page 140
        double m1 = cos(lat1) / sqrt(1.0 - pow(e * sin(lat1), 2.0));

        // Formula 3-21, page 140
        double M1 = computeM(lat1, e) * a;

        // Formula 19-12,
        double p = sign(lat1) * sqrt(lon * lon + square(a * m1 / sin(lat1) - lat));

        // Formula 19-13
        double M = a * m1 / sin(lat1) + M1 - p;

        // Formula 7-19
        double u = M / (a * (1.0 - e2 / 4.0 - 3.0 * e4 / 64.0 - 5.0 * e6 / 256.0));

        // o Formula 3-24
        double et = sqrt(1.0 - e * e);
        double e1 = (1.0 - et) / (1.0 + et);

        // o Formula 3-26
        double Lat = computeT(u, e1);

        // o Formula 14-15
        double m = cos(Lat) / sqrt(1.0 - pow(e * sin(Lat), 2.0));

        // o Formula 19-14
        double Lon = lon0 + p * atan(lon / (a * m1 / sin(lat1) - lat)) / (a * m);

        mp.lat = Lat * toDegrees;
        mp.lon = Lon * toDegrees;
    }
}
