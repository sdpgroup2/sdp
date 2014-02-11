
/** @author Jaroslaw Hirniak */

package group2.sdp.pc.geom;

import java.awt.geom.Rectangle2D;

import lejos.geom.Rectangle;


public class Plane
{
    private String id = null;
    private PointSet outline = null;
    private double eps = 1e-9;
    private Rectangle boundary = null;

    public Plane(String id) {
        this.id = id;
        outline = new PointSet(false);
    }

    public String getId() {
    	return id;
    }

    public void addPoint(Point p) {
    	outline.add(p);
    }

    public void addPoint(double x, double y) {
    	outline.add(new Point(x, y));
    }

    public boolean isWellFormed() {
    	return outline.size() > 2;
    }

    public boolean contains(Point p) {
        outline.sort();
        int N = outline.size();
        double angle = 0.0;
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);

        Point curPoint;

        for (int i = 0; i < N; i++)
        {
        	curPoint = outline.get(i);
            p1.setX(curPoint.getX() - p.getX());
            p1.setY(curPoint.getY() - p.getX());
            p2.setX(outline.get((i + 1) % N).getX() - p.getX());
            p2.setY(outline.get((i + 1) % N).getY() - p.getY());
            angle += p1.getAngle(p2);
        }

        return Math.PI - Math.abs(angle) < eps;
    }
    
    public Line getTrajectory(Point origin, double direction)
    {
    	
    }
    
    public boolean intersects(Line m, Line n)
    {
    	return m.intersectsLine(n);
    }
    
    public Line project(Point origin, double direction)
    {
    	Rectangle boundary = getBoundary();
    	Line line = expand(origin, direction);
    	
    	for (int i = 1; i < outline.size(); i++)
    	{
    		Point p0 = outline.get(i - 1);
    		Point p1 = outline.get(i);
    		
    		Line wall = new Line();
    		wall.x1 = p0.x;
    		wall.x2 = p1.x;
    		wall.y1 = p0.y;
    		wall.y2 = p1.y;
    		
    		if (intersects(line, wall))
    		{
    			// find point of intersection
    			Line.
    		}
    	}
    }
    
    public Rectangle getBoundary()
    {
    	if (boundary == null)
    	{
	    	float minX = Float.MAX_VALUE;
	    	float minY = Float.MAX_VALUE;
	    	float maxX = Float.MIN_VALUE;
	    	float maxY = Float.MIN_VALUE;
	    	
	    	for (Point p : outline.getPoints())
	    	{
	    		minX = Math.min(minX, p.x);
	    		minY = Math.min(minY, p.y);
	    		maxX = Math.max(maxX, p.x);
	    		maxY = Math.max(maxY, p.y);
	    	}
	    	
	    	boundary = new Rectangle(minX, minY, maxX - minX, maxY - minY);
    	}
    	
    	return boundary;
    }
    
    public Line expand(Point origin, double direction)
    {
    	boundary = getBoundary();
    	double width = boundary.getWidth();
    	double height = boundary.getHeight();
    	Line line = new Line();
    	
    	line.x1 = origin.x;
    	line.y1 = origin.y;
    	line.x2 = (float) (origin.x + width * Math.tan(direction));
    	line.y2 = (float) (origin.y + height * Math.tan(direction));

    	return line;
    }

}
