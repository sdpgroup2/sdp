package group2.sdp.pc.world;

import group2.sdp.pc.geom.Vector;


public interface MovingObject extends StaticObject {

	public Vector getVelocity();

	public boolean isNearWall(Pitch pitch, double distance);

}
