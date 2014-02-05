package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Velocity;
import group2.sdp.pc.vision.Pitch;

public abstract class MovingObjectAdapter extends StaticObjectAdapter implements MovingObject {

	private Velocity velocity;
	private Rect boundingRect;

	public Velocity getVelocity() {
		return this.velocity;
	}

	public boolean isNearWall(Pitch pitch, double distance) {
		return false;
	}

	public MovingObjectAdapter(Rect boundingRect, Velocity velocity) {
		this.boundingRect = boundingRect;
		this.velocity = velocity;
	}
}
