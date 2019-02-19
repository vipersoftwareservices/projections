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

public class Polyconic extends MapProjection {

    public Polyconic() {
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
     * @param lat
     * @param E
     * @return
     */
    public double computeMP(double lat, double E) {

        double E2 = E * E;
        double E4 = E2 * E2;
        double E6 = E4 * E2;

        return (1.0 - E2 / 4.0 - 3.0 * E4 / 64.0 - 5.0 * E6 / 256.0)
                - 2.0 * (3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0) * cos(2.0 * lat)
                + 4.0 * (15.0 * E4 / 256.0 + 45.0 * E6 / 1024.0) * cos(4.0 * lat) - 6.0 * (35.0 * E6 / 3072.0) * cos(6.0 * lat);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to Radians
        double lon = mp.lon * toRadians;
        double lat = mp.lat * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();
        double E = getEccentricity();

        // Formula 3-21
        double M = computeM(lat, E);

        // Formula 3-21
        double M0 = computeM(lat0, E);

        // Formula 4-20
        double slat = sin(lat);
        double N = 1.0 / sqrt(1.0 - E * E * slat * slat);

        // Formula 18-2
        double T = (lon - lon0) * sin(lat);

        // Formula 18-12
        pp.lon = a * N * cot(lat) * sin(T);

        // Formula 18-13
        pp.lat = a * (M - M0 + N * cot(lat) * (1.0 - cos(T)));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        // o Degrees to radians
        double lon = pp.lon;
        double lat = pp.lat;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double a = getRadius();
        double E = getEccentricity();
        double E2 = E * E;

        double CONVERGENCE = (1.0 / 3600.0) * toRadians;

        // Formula 3-21
        double M0 = a * computeM(lat0, E);

        // Formula 7-12
        if (lat == -M0) {
            mp.lat = 0.0;
            mp.lon = (lon / a - lon0) * toDegrees;
            return;
        }

        // Formula 18-18
        double A = (M0 + lat) / a;

        // Formula 18-19
        double B = lon * lon / (a * a) + A * A;

        //
        double C = 0.0;
        double Lat = A;
        double LatLast = A;

        do {
            LatLast = Lat;

            double sLat = sin(Lat);
            C = sqrt(1.0 - E2 * sLat * sLat) * tan(Lat);

            double MA = computeM(Lat, E);
            double MPN = computeMP(Lat, E);

            Lat = Lat - (A * (C * MA + 1.0) - MA - 0.5 * (MA * MA + B) * C)
                    / (E2 * sin(2.0 * Lat) * (MA * MA + B - 2.0 * A * MA) / 4.0 * C + (A - MA) * (C * MPN - 2.0 / sin(2.0 * Lat))
                            - MPN);

        } while (abs(Lat - LatLast) > CONVERGENCE);

        // Formula 18-22
        double Lon = asin(lon * C / a) / sin(Lat) + lon0;

        mp.lat = Lat * toDegrees;
        mp.lon = Lon * toDegrees;
    }
}
