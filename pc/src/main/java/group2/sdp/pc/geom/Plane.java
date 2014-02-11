
/** @author Jaroslaw Hirniak */

package group2.sdp.pc.geom;


public class Plane
{
    private String id = null;
    private PointSet outline = null;
    private double eps = 1e-9;

    public Plane(String id) {
        this.id = id;
        outline = new PointSet();
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
    
//    public Line getTrajectory(Point origin, double direction)
//    {
//    	
//    }

}
