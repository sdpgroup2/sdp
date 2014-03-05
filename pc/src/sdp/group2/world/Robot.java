package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Debug;


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
    private double direction = 0.0;
    private Vector facingVector = new Vector(0, 1); // TODO: <-- That is not pretty
    private Vector previousDetectedFacing = null;

    public Robot() {
        super();
    }

    public Robot(Rect boundingRect, Vector facingVector) {
        this();
        setBoundingRect(boundingRect);
        updatePosition(boundingRect.getCenter());
        setFacingVector(facingVector);
    }

    public double getRadius() {
        return RADIUS;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
//    	System.out.println("** Angle vectors **");
////    	System.out.println(facingVector);
//    	System.out.println(new Vector(1, 0));
//    	System.out.println("** --- **");
        return facingVector.angleDegrees(new Vector(1, 0));
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
