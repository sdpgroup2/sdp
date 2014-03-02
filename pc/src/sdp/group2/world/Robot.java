package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
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
    private double direction = 0.0;
    private Vector facingVector;
    private Point position = null;

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
        return direction;
    }
    
    public void update(Tuple<Point, Point> tuple) {
    	this.position = tuple.getFirst().toMillis();
    	Point dotPosition = tuple.getSecond();
    	if (dotPosition != null) {
    		this.facingVector = position.sub(dotPosition.toMillis());
    	}
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        this.position = p;
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
