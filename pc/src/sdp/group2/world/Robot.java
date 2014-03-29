package sdp.group2.world;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Debug;
import sdp.group2.util.Tuple;


/**
 * Robot is an object representing a robot on the pitch. It has the following main properties:
 * - position - its position on the pitch
 * - direction - its direction on the pitch
 * - facingVector - the vector from the middle of the dot through the body of i
 */
public class Robot extends MovableObject {

    private static final double RADIUS = 60.0;
    /**
     * [mm], measured as robot width from kicker to the back through its centre divided by 2
     */
    private Vector facingVector;
    private Vector previousDetectedFacing;
    private Zone zone;    

    public Robot(Point robotPosition, Point dotPosition, Zone zone) {
    	updatePosition(robotPosition);
    	updateFacing(dotPosition);
    	this.zone = zone;
    }
    
    public Zone getZone() {
    	return zone;
    }
    
    public double getRadius() {
        return RADIUS;
    }

    public double getDirection() {
        return facingVector.angleDegrees(new Vector(1, 0));
    }
    
    public void updateState(Point position, Point dotPosition) {
    	updatePosition(position);
    	if (dotPosition != null) {
    		updateFacing(dotPosition);
    	}
    }
    
    public void updateState(Tuple<Point, Point> tuple) {
    	updateState(tuple.getFirst(), tuple.getSecond());
    }

    public void updateFacing(Point dotPosition) {
		Vector newFacing = getPosition().sub(dotPosition);
		if (previousDetectedFacing != null) {
			double deltaAngle = newFacing.angleDegrees(previousDetectedFacing);
			if (deltaAngle > 30) {
				Debug.log("ANGLE TOO BIG!!!");
				previousDetectedFacing = newFacing;
				return;
			}
		}
		facingVector = newFacing;
		previousDetectedFacing = newFacing;
    }

    public Vector getFacingVector() {
        return facingVector;
    }

    public void setFacingVector(Vector facingVector) {
        this.facingVector = facingVector;
    }
    
    public double angleToX() {
    	return facingVector.angleDegrees(new Vector(1, 0));
    }
    
    /**
     * Returns the angle from the robot to the ball
     * @param ball
     * @return
     */
    public double angleTo(Ball ball) {
    	return angleTo(ball.getPosition());
    }
    
    public void forward(int dir, int distance) {
    	// TODO: change the name in the method below
    	CommandQueue.add(Commands.move(dir, 1500, distance), "SDP2D");
    }
    
    public void rotate(double degrees) {
    	// TODO: change the name in the method below
    	CommandQueue.add(Commands.rotate((int) degrees, 100), "SDP2D");
    }
    
    /**
     * Goes to a point. The facingForward parameter needs
     * to be true if we require the robot to be facing the
     * point when it gets to it.
     * @param point - point we are going to
     * @param facingForward - do we need to be facing the point
     */
    public void goTo(Point point, boolean facingForward) {
    	// First check how much we need to rotate
    	Vector vectorToBall = vectorTo(point);
    	// Get degrees to turn to ball
    	double angleToBall = facingVector.angleDegrees(vectorToBall);
    	double unsignedAngle = Math.abs(angleToBall);
    	double angleSign = Math.signum(angleToBall);
    	// Going backwards requires less rotating if angle above 90
    	// so we do that if we don't need to be facing forward
    	if (!facingForward && unsignedAngle > 90) {
    		// becomes -60 if it was 120
    		// and 60 if it was -120
    		angleToBall = angleSign * (180 - unsignedAngle);
    	}
    	rotate(angleToBall);
    	// Then go forward until we hit it
    	int distance = (int) vectorTo(point).length();
    	int dir = 1;
    	forward(dir, distance);
    }
    
    public double getSmallerAngle(Point point) {
    	Vector vectorToPoint = vectorTo(point);
//    	System.out.println(vectorToPoint);
    	double angleToBall = facingVector.angleDegrees(vectorToPoint);
//    	System.out.println(angleToBall);
    	double unsignedAngle = Math.abs(angleToBall);
    	double angleSign = Math.signum(angleToBall);
    	if (unsignedAngle > 90) {
    		// becomes -60 if it was 120
    		// and 60 if it was -120
    		angleToBall = angleSign * (180 - unsignedAngle);
    	}
    	return angleToBall;
    }
    
    /**
     * Makes the robot go to the ball.
     * @param ball - ball to go to
     * @param facingForward - do we need to face it?
     */
    public void goTo(Ball ball, boolean facingForward) {
    	this.goTo(ball.getPosition(), facingForward);
    }
    
    /**
     * Returns the angle from the robot to a point;
     */
    public double angleTo(Point point){
    	return facingVector.angleDegrees(vectorTo(point));
    }

    public Vector vectorTo(Ball ball) {
    	return vectorTo(ball.getPosition());
    }
    
    
    public Vector vectorTo(Point p){
    	return p.sub(getPosition());
    }

}
