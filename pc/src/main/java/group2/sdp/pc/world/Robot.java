package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

public class Robot extends RectangularObjectAdapter implements MovingObject {

	private Vector velocity;

	public Robot(Rect boundingRect, Vector velocity) {
		super(boundingRect);
		this.velocity = velocity;
	}

	@Override
	public Vector getVelocity() {
		return this.velocity;
	}

	@Override
	public boolean isNearWall(Pitch pitch, double distance) {
		return false;
	}

}
