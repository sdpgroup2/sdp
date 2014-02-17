package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;

public class Robot extends RectangularObjectAdapter implements MovingObject {

	private Point previousPosition;
	private Vector direction;
	
	public Robot(Rect boundingRect, Vector direction) {
		super(boundingRect);
		this.direction = direction;
		this.previousPosition = boundingRect.getCenter();
	}
	
	public Vector getDirection() {
		return direction;
	}
	
	@Override
	public Vector getVelocity() {
		return this.position.sub(previousPosition);
	}

	@Override
	public boolean isNearWall(PitchM pitch, double distance) {
		return false;
	}
	
	public double angleToVector(Vector destinationVector) {
		double radians = this.direction.angle(destinationVector);
		return Math.toDegrees(radians);
	}
	
	@Override
	public void setPosition(Point robotPosition) {
		this.previousPosition = this.position;
		this.position = robotPosition;
	}
	
	public void setDirection(Vector direction) {
		this.direction = direction;
	}

}
