package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Velocity;
import group2.sdp.pc.vision.Pitch;

public class Ball extends MovingObjectAdapter {

	public Ball(Rect boundingRect, Velocity velocity) {
		super(boundingRect, velocity);
	}

	@Override
	public boolean isNearWall(Pitch pitch, double distance) {
		return false;
	}

}
