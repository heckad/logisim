/* Copyright (c) 2010, Carl Burch. License information is located in the
 * com.cburch.logisim.LogisimMain source code and at www.cburch.com/logisim/. */

package com.heckad.draw.shapes;

import com.heckad.logisim.data.Location;

public class LineUtil {
    private LineUtil() {
    	
    }

    // a value we consider "small enough" to equal it to zero:
    // (this is used for double solutions in 2nd or 3d degree equation)
    private static final double zeroMax = 0.0000001;

    public static double distanceSquared(double x0, double y0, double x1, double y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        return dx * dx + dy * dy;
    }

    public static double distance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(distanceSquared(x0, y0, x1, y1));
    }

    public static double ptDistSqSegment(double x0, double y0,
            double x1, double y1, double xq, double yq) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double len2 = dx * dx + dy * dy;
        // the "segment" is essentially a point
        if (len2 < zeroMax * zeroMax) {
            return distanceSquared(xq, yq, (x0 + x1) / 2, (y0 + y1) / 2);
        }

        double u = ((xq - x0) * dx + (yq - y0) * dy) / len2;
        if (u <= 0) {
            return distanceSquared(xq, yq, x0, y0);
        }

        if (u >= 1) {
            return distanceSquared(xq, yq, x1, y1);
        }

        return distanceSquared(xq, yq, x0 + u * dx, y0 + u * dy);
    }

    public static double[] nearestPointSegment(double xq, double yq,
            double x0, double y0, double x1, double y1) {
        return nearestPoint(xq, yq, x0, y0, x1, y1, true);
    }

    public static double[] nearestPointInfinite(double xq, double yq,
            double x0, double y0, double x1, double y1) {
        return nearestPoint(xq, yq, x0, y0, x1, y1, false);
    }

    private static double[] nearestPoint(double xq, double yq,
            double x0, double y0, double x1, double y1, boolean isSegment) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double len2 = dx * dx + dy * dy;
        if (len2 < zeroMax * zeroMax) {
            // the "line" is essentially a point - return that
            return new double[] { (x0 + x1) / 2, (y0 + y1) / 2 };
        }

        double num = (xq - x0) * dx + (yq - y0) * dy;
        double u;
        if (isSegment) {
            if (num < 0) {
                u = 0;
            } else if (num < len2) {
                u = num / len2;
            } else {
                u = 1;
            }

        } else {
            u = num / len2;
        }
        return new double[] { x0 + u * dx, y0 + u * dy };
    }

    public static Location snapTo8Cardinals(Location from, int mx, int my) {
        int px = from.getX();
        int py = from.getY();
        if (mx != px && my != py) {
            double ang = Math.atan2(my - py, mx - px);
            int d45 = (Math.abs(mx - px) + Math.abs(my - py)) / 2;
            int d = (int) (4 * ang / Math.PI + 4.5);
            switch (d) {
            // going west
            case 0: case 8:
            // going east
            case 4:
                return Location.create(mx, py);
            // going north
            case 2:
            // going south
            case 6:
                return Location.create(px, my);
            // going northwest
            case 1:
                return Location.create(px - d45, py - d45);
            // going northeast
            case 3:
                return Location.create(px + d45, py - d45);
            // going southeast
            case 5:
                return Location.create(px + d45, py + d45);
            // going southwest
            case 7:
                return Location.create(px - d45, py + d45);
            default:
            	break;
            }
        }
        // should never happen
        return Location.create(mx, my);
    }

}