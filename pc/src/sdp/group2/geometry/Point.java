
/** @author Jaroslaw Hirniak */

package sdp.group2.geometry;

import java.awt.geom.Point2D;

/**
 * 2D Point.
 * Implements lexicographical order.
 */

public class Point extends Point2D.Double implements Comparable<Point> {

	private static final long serialVersionUID = 13L;
	private long timestamp;
	
    public Point(double x, double y) {
    	super(x, y);
    	this.timestamp = System.currentTimeMillis();
    }

	public double getAngle(Point other) {
        double dtheta, theta1, theta2;

        theta1 = Math.atan2(this.getY(), this.getX());
        theta2 = Math.atan2(other.getY(), other.getX());
        dtheta = theta2 - theta1;

        while (dtheta > Math.PI)  { dtheta -= 2 * Math.PI; }
        while (dtheta < -Math.PI) { dtheta += 2 * Math.PI; }

        return dtheta;
    }

    public boolean less(Point other) {
    	return this.compareTo(other) == -1;
    }

    @Override
    public boolean equals(Object other) {
    	return this.compareTo((Point) other) == 0;
    }

    @Override
    public String toString() {
    	return "(" + x + ", " + y + ")";
    }
    
    public Point toMillis() {
    	return new Point(Milimeter.pix2mm(x), Milimeter.pix2mm(y));
    }

    @Override
    public int compareTo(Point other) {
        if (this.x < other.getX()) {
        	return -1;
        } else if (this.x == other.x && this.y < other.y) {
        	return -1;
        } else if (this.x == other.x && this.y == other.y) {
        	return 0;
        } else {
        	return 1;
        }
    }
    
    public void setX(double x) {
    	this.x = x;
    }

    public void setY(double y) {
    	this.y = y;
    }
    
    public long getTimestamp() {
    	return timestamp;
    }

    public Vector sub(Point other) {
    	return new Vector(this.x - other.x, this.y - other.y);
    }

}
