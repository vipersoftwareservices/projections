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

public class Mercator extends MapProjection {

    public Mercator() {
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
                - (3.0 * E2 / 8.0 + 3.0 * E4 / 32.0 + 45.0 * E6 / 1024.0) * Math.sin(2.0 * lat)
                + (15.0 * E4 / 256.0 + 45.0 * E6 / 1024.0) * Math.sin(4.0 * lat) - (35.0 * E6 / 3072.0) * Math.sin(6.0 * lat);
    }

    /**
     * 
     * @param U
     * @param E
     * @return
     */
    public double computeT(double U, double E) {

        double E2 = E * E;
        double E3 = E2 * E;
        double E4 = E3 * E;

        return U + (3.0 * E / 2.0 - 27.0 * E3 / 32.0) * sin(2.0 * U) + (21.0 * E2 / 16.0 - 55.0 * E4 / 32.0) * sin(4.0 * U)
                + (151.0 * E3 / 96.0) * sin(6.0 * U) + (1097.0 * E4 / 512.0) * sin(8.0 * U);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void toProjection(MapPoint mp, MapPoint pp) {

        // Convert Degrees to Radians
        double lon = mp.lon * toRadians;
        double lat = mp.lat * toRadians;
        double lon0 = getOriginLon() * toRadians;

        // Formula 7-7. Set B = 1, Reduces to spherical formulas.
        double B = 1.0;
        double E = getEccentricity();

        if (E != 0.0) {
            B = E * Math.sin(lat);
            B = Math.pow(((1.0 - B) / (1.0 + B)), (E / 2.0));
        }

        // Formula 7-2, 7-7
        double Lat = 0.0;
        if (mp.lat == 90.0) {
            Lat = PI;
        } else if (mp.lat == -90.0) {
            Lat = -PI;
        } else {
            Lat = Math.log(B * Math.tan(lat / 2.0 + PI / 4.0));
        }

        // Formula 7-1.
        if (Lat > PI) {
            Lat = PI;
        } else if (Lat < -PI) {
            Lat = -PI;
        }

        pp.lat = Lat * this.radius;

        // Also, see Page 41, values must be in range -PI to PI.
        double Lon = ((lon - lon0 - PI) % (2.0 * PI)) + PI;

        pp.lon = Lon * this.radius;
    }

    /**
     * {@inheritDoc}
     *
     * Note: The Eccentricity and Standard Parrallel values are not implemented in this version. The
     * Formula assumes that these two values are at thier defaults.
     */
    @Override
    public void toLatLon(MapPoint pp, MapPoint mp) {

        double lon0 = getOriginLon();
        double a = getRadius();

        // Formula 7-1, Self reversed.
        double lon = pp.lon * toDegrees / a + lon0;

        // Formula 7-10, Page 44.
        double R1 = exp(-(pp.lat / a));

        // Formulas on Page 45, Formula 7-13
        double X1 = PI / 2.0 - 2.0 * atan(R1);

        // Coeffecients A thru C are on Page 15 Formula 3-5.
        if (eccentricity != 0.0) {

            double E = eccentricity;
            double E2 = E * E;
            double E4 = E2 * E2;
            double E6 = E4 * E2;
            double E8 = E6 * E2;

            double A = E2 / 2.0 + 5.0 * E4 / 24.0 + E6 / 12.0 + 13.0 * E8 / 360.0;
            double B = 7.0 * E4 / 48.0 + 29.0 * E6 / 240.0 + 811.0 * E8 / 11520.0;
            double C = 7.0 * E6 / 120.0 + 81.0 * E8 / 1120.0;
            double D = 4279.0 * E8 / 161280.0;

            // General Form : Page 19 Formula 3-34.
            // Specific Form : Page 15, Formula 3-6.
            X1 = X1 + A * sin(2.0 * X1) + B * sin(4.0 * X1) + C * sin(6.0 * X1) + D * sin(8.0 * X1);
        }

        double lat = X1 * toDegrees;

        mp.lon = ((lon - 180.0) % 360.0) + 180.0;
        mp.lat = ((lat - 90.0) % 180.0) + 90.0;
    }

    public String toString() {
        return "\t" + getClass().getName() + "\n" + "\tEccentricity=" + getEccentricity() + "\n" + "\tOriginLon=" + getOriginLon()
                + "\n" + "\tRadius=" + getRadius() + "\n";
    }
}
