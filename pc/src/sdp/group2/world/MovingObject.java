package sdp.group2.world;

import sdp.group2.geometry.Vector;


public interface MovingObject extends StaticObject {

	public Vector getVelocity();

	public boolean isNearWall(PitchM pitch, double distance);

}
