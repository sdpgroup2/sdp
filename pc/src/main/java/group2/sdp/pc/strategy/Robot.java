package group2.sdp.pc.strategy;

import group2.sdp.pc.world.MovableObject;

public class Robot extends MovableObject {
	
	private static final double RADIUS = 60.0; /** [mm], measured as robot width from kicker to the back through its centre divided by 2 */
	
	public double getRadius()
	{ return RADIUS; }
}
