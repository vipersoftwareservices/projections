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

import java.util.ArrayList;
import java.util.List;

public class DataList {

    class DataItem {
        String utm;
        MapPoint mp; // Map point (lat lon).
        MapPoint pp; // Projection point.
    }

    private List<DataItem> list = new ArrayList<DataItem>();

    public static DataList createInstance() {

        return new DataList();
    }

    public void add(double ilat, double ilon, double olat, double olon) {
        DataItem item = new DataItem();

        item.mp = new MapPoint(ilat, ilon);
        item.pp = new MapPoint(olat, olon);

        list.add(item);
    }

    public void add(double olat, double olon, String utm) {
        DataItem item = new DataItem();

        item.utm = utm;
        item.pp = new MapPoint(olat, olon);

        list.add(item);
    }

    public MapPoint getLatLon(int index) {

        if (index < 0 || index >= list.size()) {
            return null;
        }

        DataItem item = (DataItem) list.get(index);

        return item.mp;
    }

    public MapPoint getProjection(int index) {

        if (index < 0 || index >= list.size()) {
            return null;
        }

        DataItem item = (DataItem) list.get(index);

        return item.pp;
    }

    public int size() {
        return list.size();
    }
}
