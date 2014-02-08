package group2.sdp.pc.geom;

import java.awt.geom.Line2D;

public class Line {

	/**
	 * A 2-dimensional line defined by two vector points.
	 */
	
	private VecI[] points;
	
	public Line(VecI point0, VecI point1) {
		this.points = new VecI[2];
		points[0] = point0;
		points[1] = point1;
	}
	
	public VecI[] getPoints()
	{
		return points;
	}
	
	public boolean intersects(Line line2) {
		return Line2D.linesIntersect(points[0].x, points[0].y,
				points[1].x, points[1].y, line2.getPoints()[0].x,
				line2.getPoints()[0].y, line2.getPoints()[1].x, 
				line2.getPoints()[1].y);
	}
	
}
