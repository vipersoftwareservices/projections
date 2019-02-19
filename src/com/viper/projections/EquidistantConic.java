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

public class EquidistantConic extends MapProjection {

	public EquidistantConic() {
	}


    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toProjection(MapPoint mp, MapPoint pp) {

		//  o Convert Degrees to Radians
		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;
		double a = getRadius();

		//  o Formula 16-4.
		double N = (cos(lat1) - cos(lat2)) / (lat2 - lat1);

		//  o Formula 16-3
		double G = cos(lat1) / N + lat1;

		//  o Formula 16-2 
		double P0 = a * (G - lat0);

		//  o Formula 14-4
		double theta = N * (lon - lon0);

		//  o Formula 16-1
		double P = a * (G - lat);

		//  o Formula 14-1
		pp.lon = P * sin(theta);

		//  o Formula 16-6
		pp.lat = P0 - P * cos(theta);
	}


    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toLatLon(MapPoint pp, MapPoint mp) {

		//  o Convert Degrees to Radians
		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat0 = getOriginLat() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double lat2 = getParallel2() * toRadians;
		double a = getRadius();
		double E = getEccentricity();

		//  o Formula 16-4.
		double N = (cos(lat1) - cos(lat2)) / (lat2 - lat1);

		//  o Formula 16-3
		double G = cos(lat1) / N + lat1;

		//  o Formula 16-2
		double P0 = a * (G - lat0);

		//  o Formula 14-4
		double theta = atan(lon / (P0 - lat));

		//  o Formula 16-1, Note Radius multiplied later.
		double P = sign(N) * sqrt(lon * lon + pow(P0 - lat, 2.0));

		//  o Formula 14-2
		double Lat = G - P / a;

		//  o Formula 14-1.
		double Lon = lon0 + theta / N;

		mp.lat = Lat * toDegrees;
		mp.lon = Lon * toDegrees;

	}
}
