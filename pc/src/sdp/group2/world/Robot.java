package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Vector;


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

    public Robot(Point robotPosition, Point dotPosition) {
    	updatePosition(robotPosition);
    	updateFacing(dotPosition);
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
    
    public void updateFacing(Point dotPosition) {
    	this.facingVector = getPosition().sub(dotPosition);
    }

    public Vector getFacingVector() {
        return facingVector;
    }

    public void setFacingVector(Vector facingVector) {
        this.facingVector = facingVector;
    }

    public double angleToVector(Vector destinationVector) {
        return this.facingVector.angleDegrees(destinationVector);
    }
    
    /**
     * Returns the angle from the robot to the ball
     * @param ball
     * @return
     */
    public double angleToBall(Ball ball){
    	return this.angleToVector(vectorTo(ball));
    }
    
    /**
     * Returns the angle from the robot to a point;
     */
    public double angleToPoint(Point point){
    	return this.angleToVector(vectorTo(point));
    }

    public Vector vectorTo(MovableObject object) {
        return object.getPosition().sub(getPosition());
    }
    
    //
    public Vector vectorTo(Point p){
    	return p.sub(getPosition());
    }
    


}
