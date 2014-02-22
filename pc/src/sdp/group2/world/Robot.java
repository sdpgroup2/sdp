package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
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

    public Vector vectorTo(MovableObject object) {
        return object.getPosition().sub(getPosition());
    }

}
