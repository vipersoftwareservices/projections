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

public class VerticalPerspective extends MapProjection {

	private double P0 = 2.0 * EquatorialRadius;

	private double pAzimuth = 0.0;

	public double getPDistance() {
		return this.P0;
	}

	public void setPDistance(double P0) {
		this.P0 = P0;
	}

	public double getPAzimuth() {
		return this.pAzimuth;
	}

	public void setPAzimuth(double pAzimuth) {
		this.pAzimuth = pAzimuth;
	}


    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toProjection(MapPoint mp, MapPoint pp) {

		//  o Convert degree values to radians.
		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		double P = getPDistance() / a;
		double AZ = getPAzimuth() * toRadians;

		//  o Formula 5-3.
		double cosC = sin(lat1) * sin(lat) + cos(lat1) * cos(lat)
				* cos(lon - lon0);

		//  o Formula 23-3.
		double k = (P - 1.0) / (P - cosC);

		//  o Formula 22-4.
		pp.lon = a * k * cos(lat) * sin(lon - lon0);

		//  o Formula 22-5. 
		pp.lat = a * k
				* (cos(lat1) * sin(lat) - sin(lat1) * cos(lat) * cos(lon));
	}


    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toLatLon(MapPoint pp, MapPoint mp) {

		//  o Convert to Radians
		double lat = pp.lat / getRadius();
		double lon = pp.lon / getRadius();
		double lat1 = getParallel1() * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double a = getRadius();

		double P = getPDistance() / a;
		double AZ = getPAzimuth() * toRadians;

		//  o Formula 20-18.
		double p = sqrt(lon * lon + lat * lat);
		if (p == 0.0) {
			mp.lat = getParallel1();
			mp.lon = getOriginLon();
			return;
		}

		//  o Formula 23-4
		double c = asin((P - sqrt(1.0 - p * p * (P + 1.0) / (a * a * (P - 1.0))))
				/ (a * (P - 1.0) / p + p / (a * (P - 1.0))));
		if (P < 0.0 && p > (a * (P - 1.0) / P)) {
			c = c - PI;
		}

		//  o Formula 20-14
		double Lat = asin(cos(c) * sin(lat1) + lat * sin(c) * cos(lat1) / P);

		//  o Formula 20-15
		double num = lon * sin(c);
		double den = p * cos(lat1) * cos(c) - lat * sin(lat1) * sin(c);

		double Lon = lon0 + atan(num / den);

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;
	} 
    
	public String toString() {
		return "\t" + getClass().getName() + "\n" + "\tEccentricity="
				+ getEccentricity() + "\n" + "\tOriginLon=" + getOriginLon()
				+ "\n" + "\tRadius=" + getRadius() + "\n";
	}
}
