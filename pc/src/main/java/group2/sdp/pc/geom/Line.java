package group2.sdp.pc.geom;

import java.awt.geom.Line2D;

/**
 * A 2-dimensional line defined by two vector points.
 */
public class Line extends Line2D.Float {

	private static final long serialVersionUID = 1L;
	
	public Line(Point p1, Point p2)
	{
		super.x1 = (float) p1.x;
		super.y1 = (float) p1.y;
		super.x2 = (float) p2.x;
		super.y2 = (float) p2.y;
	}

}
