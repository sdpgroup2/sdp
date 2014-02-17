package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

public class RobotM extends RectangularObjectAdapter implements MovingObject {

	private Point previousPosition;
	private Vector direction;
	
	public RobotM(Rect boundingRect, Vector direction) {
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
