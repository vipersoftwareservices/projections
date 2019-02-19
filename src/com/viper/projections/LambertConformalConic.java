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

public class LambertConformalConic extends MapProjection {

    public LambertConformalConic() {
    }

    /**
     * 
     * @param P
     * @param E
     * @return
     */
    public double computeM(double P, double E) {

        double A = E * sin(P);

        return cos(P) / sqrt(1.0 - A * A);
    }

    /**
     * 
     * @param P
     * @param E
     * @return
     */
    public double computeT(double P, double E) {

        double S = sin(P);

        double A = (1.0 - S) / (1.0 + S);

        double ES = E * S;

        double B = (1.0 + ES) / (1.0 - ES);

        return sqrt(A * pow(B, E));
    }

    /**
     * 
     * @param SP1
     * @param SP2
     * @param ECC
     * @return
     */
    public double computeN(double SP1, double SP2, double ECC) {

        // o Formula 15-8.
        double M1 = computeM(SP1, ECC);
        double M2 = computeM(SP2, ECC);

        double T1 = computeT(SP1, ECC);
        double T2 = computeT(SP2, ECC);

        double N1 = log(M1) - log(M2);
        double N2 = log(T1) - log(T2);

        return (N2 != 0.0) ? N1 / N2 : 0.0;
    }

    /**
     * 
     * @param T
     * @param E
     * @return
     */
    public double computeP(double T, double E) {

        double E2 = pow(E, 2.0);
        double E4 = pow(E, 4.0);
        double E6 = pow(E, 6.0);
        double E8 = pow(E, 8.0);

        double X = PI / 2.0 - 2.0 * atan(T);

        double c0 = E2 / 2.0 + 5.0 * E4 / 24.0 + E6 / 12.0 + 13.0 * E8 / 360.0;
        double c1 = 7.0 * E4 / 48.0 + 29.0 * E6 / 240.0 + 811.0 * E8 / 11520.0;
        double c2 = 7.0 * E6 / 120.0 + 81.0 * E8 / 1120.0;
        double c3 = 4279.0 * E8 / 161280.0;

        return X + c0 * sin(2.0 * X) + c1 * sin(4.0 * X) + c2 * sin(6.0 * X) + c3 * sin(8.0 * X);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // o Massive Conversion to radians
        double lat = mp.lat * toRadians;
        double lon = mp.lon * toRadians;
        double lat0 = getOriginLat() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double SP1 = getParallel1() * toRadians;
        double SP2 = getParallel2() * toRadians;

        double a = getRadius();
        double E = getEccentricity();

        // o Formula 15-8.
        double N = computeN(SP1, SP2, E);

        // o Formula 15-9.
        double T1 = computeT(SP1, E);

        // o Formula 15-9.
        double T0 = computeT(lat0, E);

        // o Formula 14-15.
        double M1 = computeM(SP1, E);

        // o Formula 15-10.
        double RD = N * pow(T1, N);
        double F = (RD != 0.0) ? M1 / RD : INFINITY;

        // o Formula 15-9.
        double T = computeT(lat, E);

        // o Formula 15-7.
        double P = a * F * pow(T, N);

        // o Formula 15-7a.
        double P0 = a * F * pow(T0, N);

        // o Formula 14-4.
        double Y2 = N * (lon - lon0);

        // o Formula 14-1.
        pp.lon = P * sin(Y2);

        // o Formula 14-2.
        pp.lat = P0 - P * cos(Y2);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        double lon = pp.lon;
        double lat = pp.lat;
        double SP1 = getParallel1() * toRadians;
        double SP2 = getParallel2() * toRadians;
        double lon0 = getOriginLon() * toRadians;
        double lat0 = getOriginLat() * toRadians;

        double a = getRadius();
        double E = getEccentricity();

        // o Formula 15-8.
        double N = computeN(SP1, SP2, E);

        // o Formula 15-9.
        double T1 = computeT(SP1, E);

        // o Formula 15-9.
        double T0 = computeT(lat0, E);

        // o Formula 14-5.
        double M1 = computeM(SP1, E);

        // o Formula 15-10.
        double F = M1 / (N * pow(T1, N));

        // o Formula 15-7a.
        double P0 = a * F * pow(T0, N);

        // o Formula 14-10.
        double latP = P0 - lat;
        double P = sign(N) * sqrt(lon * lon + latP * latP);

        // o Formula 14-11.
        double T2 = atan(lon / (P0 - lat));

        // o Formula 15-11.
        double T = pow((P / (a * F)), 1.0 / N);

        // o Formula's 3-5, 7-13.
        mp.lat = computeP(T, E) * toDegrees;

        // o Formula 14-9.
        mp.lon = (T2 / N) * toDegrees + getOriginLon();
    }
}
