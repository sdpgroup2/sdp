
package sdp.group2.geometry;

import java.awt.geom.Point2D;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;

import static com.googlecode.javacv.cpp.opencv_core.cvPoint;

public class Point extends Point2D.Double implements Comparable<Point> {

	private static final long serialVersionUID = 13L;
	
    public Point(double x, double y) {
    	super(x, y);
    }

    /**
     * What is this for? Use the Vector class.
     * @param other
     * @returnR
     */
	public double getAngle(Point other) {
		double theta = Math.atan2(this.y - other.y, this.x - other.x);
		
		while (theta > Math.PI)  { theta -= 2 * Math.PI; }
		while (theta <= -Math.PI) { theta += 2 * Math.PI; }
		
		System.out.println("***");
		System.out.println(this);
		System.out.println(other);
		System.out.println(theta);
		System.out.println("***");
		
		return theta;
    }
	
	public void offset(double x, double y) {
		this.x += x;
		this.y += y;
	}

    @Override
    public String toString() {
    	return "(" + x + ", " + y + ")";
    }
    
    public Point toMillis() {
    	this.x = Millimeter.pix2mm(x);
    	this.y = Millimeter.pix2mm(y);
    	return this;
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
    
    public CvPoint cv() {
    	return cvPoint((int) x, (int) y);
    }
    
    public void setX(double x) {
    	this.x = x;
    }

    public void setY(double y) {
    	this.y = y;
    }
    

    /**
     * Returns a vector between this point and other point.
     * @param other other point.
     * @return vector between the points.
     */
    public Vector sub(Point other) {
    	return new Vector(this.x - other.x, this.y - other.y);
    }

}
