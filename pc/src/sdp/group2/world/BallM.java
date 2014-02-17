package sdp.group2.world;

import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;

public class BallM extends CircularObjectAdapter implements MovingObject {

	private Vector velocity;

	public BallM(Rect boundingRect, Vector velocity) {
		super(boundingRect);
		this.velocity = velocity; // TODO: Make sure that velocity is passed from the vision, not displacement
	}

	@Override
	public boolean isNearWall(PitchM pitch, double distance) {
		return false;
	}

	@Override
	public Vector getVelocity() {
		return this.velocity;
	}

}
