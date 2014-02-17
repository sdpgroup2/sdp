package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;

public abstract class StaticObjectAdapter implements StaticObject {

	protected Point previousPosition;
	protected Point position;

	public StaticObjectAdapter(Rect boundingBox) {
		this.position = new Point(
			boundingBox.x + boundingBox.width / 2,
			boundingBox.y + boundingBox.height / 2
		);
	}

	@Override
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point pt) {
		this.position = pt;
	}

	@Override
	public abstract Rect getBoundingRect();

}
