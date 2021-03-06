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

public class Orthographic extends MapProjection {

    public Orthographic() {
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to Radians
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        // o Formula 20-3
        pp.lon = a * cos(lat) * sin(lon - lon0);

        // o Formula 20-4
        pp.lat = a * cos(lat1) * sin(lat) - sin(lat1) * cos(lat) * cos(lon - lon0);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double lat1 = getParallel1() * toRadians;
        double a = getRadius();

        // o Formula 20-18
        double P = sqrt(lon * lon + lat * lat);
        if (P == 0.0) {
            mp.lat = lat1 * toDegrees;
            mp.lon = lon0 * toDegrees;
            return;
        }

        // o Formula 20-19
        double C = asin(P / a);

        // o Formula 20-14
        double Lat = cos(C) * sin(lat1) + sin(C) * cos(lat1) * lat / P;

        mp.lat = asin(Lat) * toDegrees;

        // o Formula 20-15
        double num = 0.0;
        double den = 0.0;

        if (getParallel1() == 90.0) {
            num = lon;
            den = -lat;

        } else if (getParallel1() == -90.0) {
            num = lon;
            den = lat;

        } else {
            num = lon * sin(C);
            den = P * cos(lat1) * cos(C) - lat * sin(lat1) * sin(C);
        }

        double Lon = lon0 + atan(num / den);

        mp.lon = Lon * toDegrees;
    }
}
