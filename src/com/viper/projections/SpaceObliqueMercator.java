/*
 * --------------------------------------------------------------
 *               VIPER SOFTWARE SERVICES
 * --------------------------------------------------------------
 *
 * @(#)filename.java	1.00 2003/06/15
 *
 * Copyright 1998-2003 by Viper Software Services
 * 36710 Nichols Ave, Fremont CA, 94536
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Viper Software Services. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Viper Software Services.
 *
 * @author Tom Nevin (TomNevin@pacbell.net)
 *
 * @version 1.0, 06/15/2003 
 *
 * @note 
 *        
 * ---------------------------------------------------------------
 */

package com.viper.projections;

public class SpaceObliqueMercator extends MapProjection {

	// Landsat Parameters
	public final double LANDSAT123_INCLINE = 99.092; // Degrees

	public final double LANDSAT123_P1 = 251.0;

	public final double LANDSAT123_P2 = 18.0;

	public final double LANDSAT123_C1 = 128.87;

	public final double LANDSAT123_C2 = 251.00;

	public final double LANDSAT123_R = 7294690.0;

	public final double LANDSAT45_C1 = 129.30;

	public final double LANDSAT45_C2 = 233.00;

	double SIMPSONS[] = { 1.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 2.0, 4.0, 1.0 };

	double SList[] = new double[SIMPSONS.length];

	double HList[] = new double[SIMPSONS.length];

	int path;

	double incline = LANDSAT123_INCLINE;

	double P1 = LANDSAT123_P1;

	double P2 = LANDSAT123_P2;

	double C1 = LANDSAT123_C1; // TBD 2004

	double C2 = LANDSAT123_C2;

	double EARTH_RADIUS = EquatorialRadius;

	double convergence = (1.0 / 3600.0) * toRadians;

	/**

	 double B  = 1.004560314;
	 double A2 = -0.0009425101;
	 double A4 = -0.0000012678;
	 double A6 = -0.0000000021;
	 double C1 = 0.1375926735;
	 double C3 = 0.0000299489;
	 double C5 = 0.0000000004;
	 double R  = 7081000.0;
	 double I  = 98.20;
	 double P  = 16.0 / 233.0;
	 **/

	public SpaceObliqueMercator() {
	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public double getIncline() {
		return this.incline;
	}

	public void setIncline(double incline) {
		this.incline = incline;
	}

	public double getP1() {
		return this.P1;
	}

	public void setP1(double P1) {
		this.P1 = P1;
	}

	public double getP2() {
		return this.P2;
	}

	public void setP2(double P2) {
		this.P2 = P2;
	}

	public double getC1() {
		return this.C1;
	}

	public void setC1(double C1) {
		this.C1 = C1;
	}

	public double getC2() {
		return this.C2;
	}

	public void setC2(double C2) {
		this.C2 = C2;
	}

	public double getEarthRadius() {
		return this.EARTH_RADIUS;
	}

	public void setEarthRadius(double earthRadius) {
		this.EARTH_RADIUS = earthRadius;
	}

	public double getConvergence() {
		return this.convergence;
	}

	public void setConvergence(double convergence) {
		this.convergence = convergence;
	}

	public double getInterval() {
		return 9.0 * toRadians;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 **  o Calculation of S (page 222, Formula 27-30).
	 ** ----------------------------------------------------------
	 **/

	public double computeS(double lon, double Q, double T, double W) {

		double P1 = getP1();
		double P2 = getP2();
		double I = getIncline() * toRadians;

		double S1 = square(sin(lon));
		double S3 = (1.0 + T * S1) / ((1.0 + Q * S1) * (1.0 + W * S1));

		return (P2 / P1) * sin(I) * cos(lon) * sqrt(S3);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Calculation of A (page 222, Formula 27-22).
	 **
	 ** ----------------------------------------------------------
	 **/

	public void computeSList(double slist[]) {

		double Q = computeQ();
		double T = computeT();
		double W = computeW();

		double lon2 = 0.0;
		double dlon = getInterval();

		for (int i = 0; i < slist.length; i++) {
			slist[i] = computeS(lon2, Q, T, W);

			lon2 = lon2 + dlon;
		}
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 **  o Calculation of H (page 222, Formula 27-31).
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeH(double lon, double Q, double W) {

		double P1 = getP1();
		double P2 = getP2();
		double I = getIncline() * toRadians;

		double S1 = square(sin(lon));
		double S2 = 1.0 + Q * S1;
		double S3 = 1.0 + W * S1;

		return sqrt(S2 / S3) * ((S3 / S2 * S2) - (P2 / P1) * cos(I));
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Calculation of A (page 222, Formula 27-22).
	 **
	 ** ----------------------------------------------------------
	 **/

	public void computeHList(double hlist[]) {

		double Q = computeQ();
		double W = computeW();

		double lon = 0.0;
		double dlon = getInterval();

		for (int i = 0; i < hlist.length; i++) {
			hlist[i] = computeH(lon, Q, W);

			lon = lon + dlon;
		}
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return X2
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeLon2(MapPoint mp) {

		double lon = mp.lon * toRadians;
		double lat = mp.lat * toRadians;
		double lon0 = getOriginLon() * toRadians;
		double I = getIncline() * toRadians;
		double P1 = getP1();
		double P2 = getP2();
		double PI_2 = PI / 2.0;
		double E = getEccentricity();
		double E2 = E * E;

		double CONVERGENCE = getConvergence();

		//  o Iterative Loop Formula 27-34,27-35.
		double lon2 = 0.0;
		double lont = PI_2;

		for (int i = 0; i < 100; i++) {
			lon2 = atan(cos(I) * tan(lont) + (1.0 + E2) * sin(I) * tan(lat)
					/ cos(lont));
			lont = lon - lon0 + (P2 / P1) * lon2;

			if (abs(lon2 - lont) <= CONVERGENCE) {
				break;
			}
		}

		return lon2;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeJ() {

		double E = getEccentricity();

		return pow(1.0 - E * E, 3.0);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeQ() {

		double E = getEccentricity();
		double I = getIncline() * toRadians;
		double E2 = E * E;

		return E2 * pow(sin(I), 2.0) / (1.0 - E2);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeT() {

		double E = getEccentricity();
		double I = getIncline() * toRadians;
		double E2 = E * E;

		return E2 * pow(sin(I), 2.0) * (2.0 - E2) / pow(1.0 - E2, 2.0);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeW() {

		double E = getEccentricity();
		double I = getIncline() * toRadians;
		double E2 = E * E;

		return (pow(1.0 - E2 * pow(cos(I), 2.0), 2.0) / pow(1.0 - E2, 2.0)) - 1.0;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Calculation of A (page 222, Formula 27-22).
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeA(int n) {

		double J = computeJ();
		double N = (double) n;

		double A = 0.0;
		double lon = 0.0;
		double dlon = getInterval();

		for (int i = 0; i < SIMPSONS.length; i++) {

			double S = this.SList[i];
			double H = this.HList[i];
			double M = SIMPSONS[i];

			A = A + M * ((H * J - S * S) / sqrt(J * J + S * S)) * cos(N * lon);

			lon = lon + dlon;
		}

		return A * 4.0 / (N * PI);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Calculation of B (page 222, Formula 27-21).
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeB() {

		double J = computeJ();

		double B = 0.0;
		double lon = 0.0;
		double dlon = getInterval();

		for (int i = 0; i < SIMPSONS.length; i++) {

			double S = this.SList[i];
			double H = this.HList[i];
			double M = SIMPSONS[i];

			B = B + M * ((H * J - S * S) / sqrt(J * J + S * S));

			lon = lon + dlon;
		}

		return B * 2.0 / PI;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** Calculation of C (page 222, Formula 27-23).
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeC(int n) {

		double J = computeJ();
		double N = (double) n;

		double C = 0.0;
		double lon = 0.0;
		double dlon = getInterval();

		for (int i = 0; i < SIMPSONS.length; i++) {

			double S = this.SList[i];
			double H = this.HList[i];
			double M = SIMPSONS[i];

			C = C + M * ((S * H + S * J) / sqrt(J * J + S * S)) * cos(N * lon);

			lon = lon + dlon;
		}

		return C * 4.0 / (N * PI);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public double computeLat2(double Lat, double lon2) {

		double lat = Lat * toRadians;
		double I = getIncline() * toRadians;
		double E = getEccentricity();
		double E2 = E * E;

		//  o  page 223 Formula 27-36
		double R0 = ((1.0 - E2) * cos(I) * sin(lat) - sin(I) * cos(lat)
				* sin(lon2))
				/ sqrt(1.0 - E2 * square(sin(lat)));

		//  o Validate R0 for the Arc Sine Function.
		if (R0 < -1.0 || R0 > 1.0) {
			R0 = (R0 > 1.0) ? 1.0 : -1.0; // Probably Wrong thing to do.
			new Exception().printStackTrace();
		}

		return asin(R0);
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toProjection(MapPoint mp, MapPoint pp) {

		double R = getEarthRadius();

		double J = computeJ();
		double Q = computeQ();
		double W = computeW();
		double T = computeT();

		computeSList(SList);
		computeHList(HList);

		double A2 = computeA(2);
		double A4 = computeA(4);
		double A6 = computeA(6);
		double B = computeB();
		double C1 = computeC(1);
		double C3 = computeC(3);
		double C5 = computeC(5);

		//  o Iteration of Formulas 27-34,27-35, generates X2, XT
		double lon2 = computeLon2(mp);

		//  o Recalculate S with new Lambda Primed.
		double S = computeS(lon2, Q, T, W);

		//  o Calculate Y2
		double lonT = 0.0; // TBD
		double lat2 = computeLat2(mp.lat, lonT);

		//  o
		double M = log(tan(PI / 4.0 + lat2 / 2.0)) / sqrt(J * J + S * S);

		//  o Final Calculation of X. Page 222, Formula (27-32);
		double lon = B * lon2 + A2 * sin(2.0 * lon2) + A4 * sin(4.0 * lon2)
				+ A6 * sin(6.0 * lon2) - S * M;

		//  o Final Calculation of Y. Page 222, Formula (27-33);
		double lat = C1 * sin(1.0 * lon2) + C3 * sin(3.0 * lon2) + C5
				* sin(5.0 * lon2) + J * M;

		pp.lat = lat * R;
		pp.lon = lon * R;
	}

	/** ----------------------------------------------------------
	 **
	 **
	 ** @param
	 ** @return
	 ** @exception
	 **
	 ** ----------------------------------------------------------
	 **/

	public void toLatLon(MapPoint pp, MapPoint mp) {

		double lon0 = getOriginLon() * toRadians;
		double R = getEarthRadius();
		double E = getEccentricity();
		double I = getIncline() * toRadians;
		double E2 = E * E;
		double P = getP2() / getP1();
		double CONVERGENCE = getConvergence();

		double lon = pp.lon / R;
		double lat = pp.lat / R;

		double Q = computeQ();
		double J = computeJ();
		double T = computeT();
		double W = computeW();

		computeSList(SList);
		computeHList(HList);

		double A2 = computeA(2);
		double A4 = computeA(4);
		double A6 = computeA(6);
		double B = computeB();
		double C1 = computeC(1);
		double C3 = computeC(3);
		double C5 = computeC(5);

		//  o Formula 27-51, Involves iteration
		double lon2 = lon * B;

		//  o Iterative Loop Formula 27-34,27-35.
		for (int i = 0; i < 100; i++) {
			double XP = lon2;
			double S = computeS(XP, Q, T, W);

			lon2 = lon2
					+ ((S / J) * lat - A2 * sin(2.0 * XP) - A4 * sin(4.0 * XP) - (S / J)
							* (C1 * sin(XP) + C3 * sin(3.0 * XP))) / B;

			if (abs(XP - lon2) <= CONVERGENCE) {
				break;
			}
		}

		//  o Formula 27-52.
		double S = computeS(lon2, Q, T, W);

		double T1 = sqrt(1.0 + (S * S) / (J * J));
		double T2 = lat - C1 * sin(lon2) - C3 * sin(3.0 * lon2);
		double Y2 = 2.0 * (atan(Math.exp(T1 * T2)) - PI / 4.0);

		//  o Formula 27-48.
		double U = E2 * pow(cos(I), 2.0) / (1.0 - E2);

		//  o Formula 27-47
		double sinLat2 = square(sin(Y2));
		double V = ((1.0 - sinLat2 / (1.0 - E2)) * cos(I) * sin(lon2) - sin(I)
				* sin(Y2)
				* sqrt(1.0 + Q * square(sin(lon2)) * (1.0 - sinLat2) - U
						* sinLat2))
				/ (1.0 - sinLat2 * (1.0 + U));

		//  o Formula 27-46
		double lonT = atan2(V, cos(lon2));

		//  o Formula 27-45
		mp.lon = (lonT - P * lon2) * toDegrees + lon0;

		//  o Formula  27-49.
		double RY = 0.0;

		if (I != 0.0) {
			T1 = tan(2) * cos(lonT) - cos(I) * sin(lonT);
			T2 = (1.0 - E2) * sin(I);
			RY = atan(T1 / T2);

			//  o Formula  27-50.
		} else {
			T1 = sin(Y2);
			T2 = square(1.0 - E2) + E2 * T1 * T1;
			RY = asin(T1 / sqrt(T2));
		}

		mp.lat = RY * toDegrees;
	}
}
