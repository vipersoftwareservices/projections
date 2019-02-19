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

public class EquidistantCylindrical extends MapProjection {

	public EquidistantCylindrical() {
	}


    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toProjection(MapPoint mp, MapPoint pp) {

		double lat = mp.lat * toRadians;
		double lon = mp.lon * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 12-1
		pp.lat = lat * a;
		pp.lon = (lon - lon0) * cos(lat1) * a;
	}

    /**
     * {@inheritDoc}
     *
     */
    @Override
	public void toLatLon(MapPoint pp, MapPoint mp) {

		double lon = pp.lon;
		double lat = pp.lat;
		double lon0 = getOriginLon() * toRadians;
		double lat1 = getParallel1() * toRadians;
		double a = getRadius();

		//  o Formula 12-5
		mp.lat = (lat / a) * toDegrees;

		//  o Formula 12-6.
		double Lon = (lon0 + lon / (a * cos(lat1))) * toDegrees;

		mp.lon = ((Lon - 180.0) % 360.0) + 180.0;
	}
}
