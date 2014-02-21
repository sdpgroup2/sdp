package sdp.group2.world;

import sdp.group2.geometry.Rect;


public class Ball extends MovableObject {

	private static final double RADIUS = 25; /** [mm] */

	public Ball()
	{ }
	
	public Ball(Rect boundingRect) {
		super();
		updatePosition(boundingRect.getCenter());
	}
	
	public double getRadius()
	{ return RADIUS; }
}
