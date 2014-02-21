package sdp.group2.world;

import sdp.group2.geometry.Rect;


public class Ball extends MovableObject {

	private static final double RADIUS = 25; /** [mm] */

	public Ball() {
        super();
    }
	
	public Ball(Rect boundingRect) {
		this();
        setBoundingRect(boundingRect);
		updatePosition(boundingRect.getCenter());
	}
	
	public double getRadius()
	{ return RADIUS; }
}
