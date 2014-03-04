
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
		double theta = Math.atan2(this.y - other.y, this.x - other.x);
		
		while (theta > Math.PI)  { theta -= 2 * Math.PI; }
		while (theta < -Math.PI) { theta += 2 * Math.PI; }
		
		System.out.println("***");
		System.out.println(this);
		System.out.println(other);
		System.out.println(theta);
		System.out.println("***");
		
		return theta;
    }
	
	public void offset(Point other) {
		this.x += other.x;
		this.y += other.y;
	}
	
	public void offset(double x, double y) {
		this.x += x;
		this.y += y;
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
    	return new Point(Millimeter.pix2mm(x), Millimeter.pix2mm(y));
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
