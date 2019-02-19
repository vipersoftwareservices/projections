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

public class Stereographic extends MapProjection {

    double K0 = 1.0;

    public Stereographic() {
    }

    public double getK0() {
        return this.K0;
    }

    public void setK0(double K0) {
        this.K0 = K0;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Convert Degrees to Radians
        double lat = mp.lat * toRadians;
        double lon = (mp.lon - getOriginLon()) * toRadians;
        double SP = getParallel1() * toRadians;

        // o Formula 21-4
        double R0 = 1.0 + sin(SP) * sin(lat) + cos(SP) * cos(lat) * cos(lon);

        double K = 2.0 * this.K0 / R0;

        // o Formula 21-2
        pp.lon = getRadius() * K * cos(lat) * sin(lon);

        // o Formula 21-3
        double RY = cos(SP) * sin(lat) - sin(SP) * cos(lat) * cos(lon);

        pp.lat = getRadius() * K * RY;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        // o Convert Degrees to Radians
        double lat = pp.lat;
        double lon = pp.lon;
        double SP1 = getParallel1() * toRadians;

        // o Formula 20-18
        double P = sqrt(lon * lon + lat * lat);

        if (P == 0.0) {
            mp.lat = getParallel1();
            mp.lon = getOriginLon();
            new Exception().printStackTrace();
            return;
        }

        // o Formula 21-15, Denominator always positive.
        double C = 2.0 * atan(P / (2.0 * getRadius() * this.K0));

        // o Formula 20-14
        double RY = cos(C) * sin(SP1) + lat * sin(C) * cos(SP1) / P;

        mp.lat = asin(RY) * toDegrees;

        // o Formula 20-15
        double RD = P * cos(SP1) * cos(C) - lat * sin(SP1) * sin(C);

        double RN = lon * sin(C);

        // o Common portion of formulas 20-15, 20-16, and 20-17.
        double RX = getOriginLon() + atan2(RN, RD) * toDegrees;

        mp.lon = ((RX - 180.0) % 360.0) + 180.0;
    }
}
