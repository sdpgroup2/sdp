package group2.sdp.pc.geom;

import java.awt.geom.Line2D;

/**
 * A 2-dimensional line defined by two vector points.
 */
public class Line extends Line2D.Double {

	private static final long serialVersionUID = 1L;

	public Line(double x1, double x2, double y1, double y2) {
		super(x1, x2, y1, y2);
	}

}
