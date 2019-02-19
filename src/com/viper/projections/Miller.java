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

public class Miller extends MapProjection {

    public Miller() {
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to radians.
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();

        // o Formula 11-1.
        pp.lon = a * (lon - lon0);

        // o Formula 11-2.
        double Lat = log(tan(PI / 4.0 + lat * 0.4));

        pp.lat = a * Lat * 1.25;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        // o
        double lat = pp.lat;
        double lon = pp.lon;
        double lon0 = getOriginLon() * toRadians;
        double a = getRadius();

        // o Formula 11-7.
        mp.lon = (lon0 + lon / a) * toDegrees;

        // o Formula 11-6.
        double Lat = exp(0.8 * lat / a);

        mp.lat = (2.5 * atan(Lat) - 0.625 * PI) * toDegrees;
    }
}
