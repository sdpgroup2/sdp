package group2.sdp.pc.geom;

import java.awt.geom.Line2D;

/**
 * A 2-dimensional line defined by two vector points.
 */
public class Line extends Line2D.Double {

	private static final long serialVersionUID = 1L;
	
	public Line(Point p0, Point p1)
	{
		super(p0.x, p0.y, p1.x, p1.y);
	}

	public Line(double x1, double y1, double x2, double y2) {
		super(x1, y1, x1, y2);
	}

}
