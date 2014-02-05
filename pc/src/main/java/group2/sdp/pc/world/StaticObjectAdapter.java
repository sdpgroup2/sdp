package group2.sdp.pc.world;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class StaticObjectAdapter implements StaticObject {

	private Rectangle2D boundingRect;

	public Point2D getPosition() {
		return new Point2D.Double(boundingRect.getCenterX(), boundingRect.getCenterX());
	}

	public Rectangle2D getBoundingRect() {
		return this.boundingRect;
	}

}
