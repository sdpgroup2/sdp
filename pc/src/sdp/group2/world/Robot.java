package sdp.group2.world;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Constants;
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
    private int zone;   
    private String name;

    public Robot(Point robotPosition, Point dotPosition, int zone, String name) {
    	super(robotPosition);
    	updateFacing(dotPosition);
    	this.zone = zone;
    	this.name = name;
    }
    
    public int getZone() {
    	return zone;
    }
    
    public double getRadius() {
        return RADIUS;
    }

    public double getDirection() {
        return facingVector.angleDegrees(new Vector(1, 0));
    }
    
    public void updateState(Point position, Point dotPosition) {
    	setPosition(position);
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
//			if (deltaAngle > 30) {
//				Debug.log("ANGLE TOO BIG!!!");
//				previousDetectedFacing = newFacing;
//				return;
//			}
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
    	CommandQueue.add(Commands.move(dir, 500, distance), name);
    }
    
    public void rotate(double degrees) {
    	// TODO: change the name in the method below
    	CommandQueue.add(Commands.rotate((int) degrees, 50), name);
    }
    
    public double distanceTo(Point point) {
    	return vectorTo(point).length();
    }
    
    public double distanceTo(Ball ball) {
    	return distanceTo(ball.getPosition());
    }
    
    public boolean haveBall(Ball ball) {
    	return (distanceTo(ball) <= 80 && Math.abs(angleTo(ball)) <= 20);
    }
    
    public void kick() {
		CommandQueue.add(Commands.kick(15, 0), name);
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
    	double angleToBall = angleTo(point);
    	int direction = 1;
    	if (!facingForward) {
    		// direction will be reversed if above 90 degrees
    		direction = angleToBall <= 90 ? 1 : -1;
    		smallerAngle(point);
    	}
    	rotate(angleToBall);
    	// Then go forward until we hit it
    	int distance = (int) vectorTo(point).length();
    	forward(direction, distance);
    }
    
    /**
     * Returns the smaller signed angle to a certain point.
     * Good for going backwards if needed
     * @param point to get angle to
     * @return angle in degrees
     */
    public double smallerAngle(Point point) {
    	Vector vectorToPoint = vectorTo(point);
    	double angleToBall = facingVector.angleDegrees(vectorToPoint);
    	double unsignedAngle = Math.abs(angleToBall);
    	double angleSign = Math.signum(angleToBall);
    	if (unsignedAngle > 90) {
    		// becomes -60 if it was 120
    		// and 60 if it was -120
    		angleToBall = -angleSign * (180 - unsignedAngle);
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
