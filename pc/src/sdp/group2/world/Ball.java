package sdp.group2.world;

import sdp.group2.geometry.Point;


public class Ball extends MovableObject {

	public Ball(Point position) {
		super(position);
	}

	private static final double RADIUS = 25; /** [mm] */
	
	public double getRadius() {
		return RADIUS;
	}
}
