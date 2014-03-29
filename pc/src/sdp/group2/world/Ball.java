package sdp.group2.world;

import sdp.group2.geometry.Point;


public class Ball extends MovableObject {

	private static final double RADIUS = 25; /** [mm] */

	public Ball(Point position) {
		super(position);
	}

	public double getRadius() {
		return RADIUS;
	}
}
