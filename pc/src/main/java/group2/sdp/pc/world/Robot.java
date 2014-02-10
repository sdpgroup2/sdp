package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

public class Robot extends RectangularObjectAdapter implements MovingObject {

	private Point previousPosition;
	private Rect color;
	
	public Robot(Rect color, Rect boundingRect) {
		super(boundingRect);
		this.previousPosition = boundingRect.getCenter();
		this.color = color;
	}
	
	public Vector getDirection() {
		return super.getBoundingRect().getCenter().sub(color.getCenter());
	}
	
	public Point getColorCenter() {
		return color.getCenter();
	}

	@Override
	public Vector getVelocity() {
		return this.position.sub(previousPosition);
	}

	@Override
	public boolean isNearWall(Pitch pitch, double distance) {
		return false;
	}

}
