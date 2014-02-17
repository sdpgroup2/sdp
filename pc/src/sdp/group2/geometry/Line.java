package sdp.group2.geometry;

import java.awt.geom.Line2D;

/**
 * A 2-dimensional line defined by two vector points.
 */
public class Line extends Line2D.Double {

	private static final long serialVersionUID = 313L;
	private static double EPS = 1e-3;
	
	public Line(Point p0, Point p1)
	{
		super(p0.x, p0.y, p1.x, p1.y);
	}

	public Line(double x1, double y1, double x2, double y2) {
		super(x1, y1, x1, y2);
	}
	
	/**
	 * @param p - get perpendicular line in this Point
	 * @return perpendicular line to given at point p
	 */	
	public Line getPerpendicular(Point p)
	{
		// TODO: Add if (check if point is on line)
		
		// trim line
		double x1 = p.x;
		double y1 = p.y;
		
		double dx = x2 - x1;
		double dy = y2 - y1;
		
		double scale = 50.0 / Math.sqrt(dx*dx+dy*dy); // 50.0 is intended length of the line
		
		double dx2 = -dy * scale;
		double dy2 = dx * scale;
		
		return new Line(x2, y2, x2 + dx2, y2 + dy2);
	}
	
	/** gets perpendicular line at the end of the line (x2, y2) */ 
	public Line getPerpendicular()
	{ return getPerpendicular(new Point(x2, y2)); }
	
	public void extend(double length)
	{
		double theta = Math.atan2(y2 - y1, x2 - x1);
		y2 += Math.tan(theta) * length;
		x2 += 1.0 / Math.tan(theta) * length;
	}
	
	public boolean isOnLine(Point p)
	{
		return true; // TODO: implement
	}
	
	public double getDirection()
	{ return Math.atan2(y2 - y1, x2 - x1); }

}
