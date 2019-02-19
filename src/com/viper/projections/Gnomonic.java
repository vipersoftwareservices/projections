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

public class Gnomonic extends MapProjection {

    public Gnomonic() {
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public boolean inView(MapPoint mp) {

        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;

        double cosC = sin(lat1) * sin(lat) + cos(lat1) * cos(lat) * cos(lon - lon0);

        return (cosC > 0.0);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to rad.
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        if (getParallel1() == 90.0) {
            pp.lon = a * cot(lat) * sin(lon - lon0);
            pp.lat = -a * cot(lat) * cos(lon - lon0);
            return;

        } else if (getParallel1() == -90.0) {
            pp.lon = -a * cot(lat) * sin(lon - lon0);
            pp.lat = a * cot(lat) * cos(lon - lon0);
            return;

        }

        // o Formula 5-3.
        double cosC = sin(lat1) * sin(lat) + cos(lat1) * cos(lat) * cos(lon - lon0);

        // o Formula 22-3
        double K = 1.0 / cosC;

        // o Formula 22-4.
        pp.lon = a * K * cos(lat) * sin(lon - lon0);

        // o Formula 22-5.
        pp.lat = a * K * (cos(lat1) * sin(lat) - sin(lat1) * cos(lat) * cos(lon - lon0));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        // o Convert to Radians
        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        // o Formula 20-18.
        double P = sqrt(lon * lon + lat * lat);

        // o Formula 22-16.
        double C = atan(P / a);

        // o Formula 20-14
        double Lat = lat1;
        if (P != 0.0) {
            Lat = asin(cos(C) * sin(lat1) + lat * sin(C) * cos(lat1) / P);
        }

        // o Common portion of formulas 20-15, 20-16, and 20-17.
        double num = 0.0;
        double den = 0.0;
        if (lat1 == 90.0) {
            num = lon;
            den = -lat;

        } else if (lat1 == -90.0) {
            num = lon;
            den = lat;

        } else {
            num = lon * sin(C);
            den = P * cos(lat1) * cos(C) - lat * sin(lat1) * sin(C);

        }

        double Lon = lon0 + atan(num / den);

        mp.lat = Lat * toDegrees;
        mp.lon = Lon * toDegrees;
    }
}
