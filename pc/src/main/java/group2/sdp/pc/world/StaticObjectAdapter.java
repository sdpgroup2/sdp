package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;

import java.awt.geom.Point2D;

public abstract class StaticObjectAdapter implements StaticObject {

	protected Point position;

	public StaticObjectAdapter(Rect boundingBox) {
		this.position = new Point(
			boundingBox.x + boundingBox.width / 2,
			boundingBox.y + boundingBox.height / 2
		);
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public abstract Rect getBoundingRect();

}
