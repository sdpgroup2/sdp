package group2.sdp.pc.world;

import group2.sdp.pc.geom.Velocity;
import group2.sdp.pc.vision.Pitch;

public interface MovingObject extends StaticObject {

	public Velocity getVelocity();

	public boolean isNearWall(Pitch pitch, double distance);

}
