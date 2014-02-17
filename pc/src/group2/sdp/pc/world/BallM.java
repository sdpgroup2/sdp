package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

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
