package sdp.group2.world;

import java.util.Random;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.Vector;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.TeamSide;
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
    private boolean kickerOpen = false;
    private RobotState state = RobotState.FIND_BALL;
	
	// The min/max Y that the robot is allowed to have. (The goal posts)
	private static final double minY = 365;
	private static final double maxY = 1026;
	private boolean doing360 = false;
	
	// Align for passing
	private boolean passFromLeft = true; 

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
    
    public double angleToNegX() {
    	return facingVector.angleDegrees(new Vector(-1, 0));
    }
    
    public double angleToY() {
    	return facingVector.angleDegrees(new Vector(0, 1));
    }
    
    /**
     * Returns the angle from the robot to the ball
     * @param ball
     * @return
     */
    public double angleTo(Ball ball) {
    	return angleTo(ball.getPosition());
    }
    
    public void forward(int dir, int distance, int speed) {
    	boolean added = CommandQueue.add(Commands.move(dir, speed, distance), name);
    	if (added) { 
    		System.out.println("Went distance " + distance);
    	}
    }
    
    public void rotate(double degrees) {
    	// TODO: change the name in the method below
    	boolean added = CommandQueue.add(Commands.rotate((int) degrees, 50), name);
    	if (added) { 
    		System.out.println("Rotated by " + degrees);
    	}
    }
    
    public double distanceTo(Point point) {
    	return vectorTo(point).length();
    }
    
    public double distanceTo(Ball ball) {
    	return distanceTo(ball.getPosition());
    }
    
    public boolean hasBall(Ball ball) {
    	return (distanceTo(ball) <= 135 && Math.abs(angleTo(ball)) <= 30 ) || (distanceTo(ball) < 60 && Math.abs(angleTo(ball)) <= 60);
    }
    
    public boolean canGrab(Ball ball) {
    	return (distanceTo(ball) <= 160 && Math.abs(angleTo(ball)) <= 20);
    }
    
    public void kick() {
		boolean added = CommandQueue.add(Commands.kick(15, 0), name);
		if (added) {
			System.out.println("Kicked!");
			kickerOpen = true;
		}
    }
    
    public void openKicker() {
    	boolean added = CommandQueue.add(Commands.openKicker(), name);
    	if (added) {
    		System.out.println("Opened kicker!");
    		kickerOpen = true;
    	}
    }
    
    public void closeKicker() {
    	boolean added = CommandQueue.add(Commands.closeKicker(), name);
    	if (added) {
    		System.out.println("Closed kicker!");
    		kickerOpen = false;
    	}
    }
    
    public boolean shouldDefenceAlign() {
    	double angle = angleToX();
		double unsignedAngle = Math.abs(angle);
		
		// The angle is wrong if it is more than 10 degrees away from 90.
		return !(80 < unsignedAngle && unsignedAngle < 100);
    }
    
    public void defenceAlign() {
		// Angle ranges from -180 to 180 degrees.
		double angle = angleToX();
		double angleSign = Math.signum(angle);
		double unsignedAngle = Math.abs(angle);
		
		double toRotate = angleSign * (90 - unsignedAngle);
//		System.out.printf("Rotate by: %f.2\n", toRotate);
		System.out.println(CommandQueue.commandQueue2D.size());
		rotate(toRotate);
	}
    
    public boolean shouldPassAlign() {
    	double angle = angleToX();
		double unsignedAngle = Math.abs(angle);
		
		if (MasterController.ourSide == TeamSide.LEFT) {
			return (unsignedAngle < 10);
		} else {
			return (-170 < angle && angle < 170);
		}
    }
    
    public double passAlign() {
		// Angle ranges from -180 to 180 degrees.
    	double angle;
    	if (MasterController.ourSide == TeamSide.LEFT) {
    		angle = angleToX();
    	} else {
    		angle = angleToNegX();
    	}
    	double amount = -0.7 * angle;
		Debug.log("Angle: %f, Rotating: %f", angle, amount);
		rotate(amount); // TODO: Test if works as expected
		return amount;
	}
    
    public void alignWith(Point pt, int speed) {
		double angle = angleToX();
		double angleSign = Math.signum(angle);
		
    	double ry = getPosition().y;
		double by = pt.y;
		
		// angleSign is 1 if the robot is facing upwards and -1 if downwards.
		// ballDir is 1 if the ball is above, -1 if the ball is below.
		// dir multiplies these two to find the direction in which to move.
		int ballDir = (int) Math.signum(ry - by);
		int dir = (int) (ballDir * angleSign);
		
		int dist = (int) Math.abs(ry - by);
		
		// If the ball is outwith the range of the goal post, we only want to move as far as the goal post
		if (by <= minY) {
			dist = (int) Math.abs(minY - ry);
		} else if (by >= maxY) {
			dist = (int) Math.abs(maxY - ry);
		}
		
		// Multiply by 0.9 so that we don't hit the wall and stuff
		dist = (int) (0.9 * dist);
		
		// If the distance is too great, and the robot is roughly vertically aligned:
		forward(-dir, dist, speed);
    }
    
    public void do360() {
    	Random r = new Random();
    	int rand = r.nextInt(10);
		CommandQueue.add(Commands.rotate(400 + rand, 400), name);
    }
    
    public boolean isDoing360() {
    	return doing360;
    }
    
    public void alignWith(MovableObject obj, int speed) {
    		alignWith(obj.getPosition(), speed);
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
    	forward(direction, distance, 500);
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
    
    public boolean inCenter(Pitch pitch) {
    	return (getPosition().y - pitch.getCenter().y) <= 50;
    }
    
    public Vector vectorTo(Point p){
    	return p.sub(getPosition());
    }

	public boolean isKickerOpen() {
		return kickerOpen;
	}
	
	public RobotState getState() {
		return state;
	}
	
	public void setState(RobotState state) {
		this.state = state;
	}

	public enum RobotState {
		HAS_BALL,
		CAN_GRAB,
		FIND_BALL,
		CAN_PASS
	}
	
}
