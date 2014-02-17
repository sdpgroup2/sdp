package group2.sdp.pc.world;

import group2.sdp.pc.geom.Rect;


public class Ball extends MovableObject {

	private static final double RADIUS = 25; /** [mm] */

	public Ball()
	{ }
	
	public Ball(Rect boundingRect) {
		super();
		updatePoisition(boundingRect.getCenter());
	}
	
	public double getRadius()
	{ return RADIUS; }
}
