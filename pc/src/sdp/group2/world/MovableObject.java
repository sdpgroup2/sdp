package sdp.group2.world;

import sdp.group2.geometry.Point;

public abstract class MovableObject {

	private Point position;

	public MovableObject(Point position) {
		this.position = position;
	}
	
	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}
}
