package group2.sdp.pc.geom;

import java.awt.geom.Rectangle2D;


public class Rect extends Rectangle2D.Double {

	private static final long serialVersionUID = 1L;

	public Point getCenter() {
		return new Point(this.getCenterX(), this.getCenterY());
	}

	@Override
	public String toString() {
		return String.format("Rect((%.2f, %.2f)(%.2f, %.2f))", x, y, x + width, y + height);
	}

	public Rect(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

}
