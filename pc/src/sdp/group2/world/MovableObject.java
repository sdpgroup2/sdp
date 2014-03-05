/**
 * @author Jaroslaw Hirniak, s1143166
 */

package sdp.group2.world;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.geometry.Vector;


public class MovableObject {
	
	private static long SIGNIFICANT_TIME = 5000; /** [ms] */
	
	private PointSet history = new PointSet(false);
	private Point position;
	private boolean uptodate = true;
	private double speed = 0.0;
	private static double POSITION_EPS = 5; /** [mm] */
	
	public Point getPosition() {
		return position;
		/*if (history.size() < 1) {
			return new Point(0.0, 0.0);
		}
		else {
			return history.right();
		}*/
	}
	
	public void updatePosition(Point position) {
		this.position = position;
		return;
		/*history.add(position);
		uptodate = false;*/
	}
	
	public double getSpeed() {
		update();
		return speed;
	}
	
    /**
     * Returns the vector from this object to another object.
     * @param other object we want a vector to
     * @return vector difference of the two positions
     */
    public Vector vectorTo(MovableObject other) {
        return this.getPosition().sub(other.getPosition());
    }


	public void update() {
		if (uptodate || history.size() < 2) {
            return;
        }
		
		/* Compute speed and direction */
		double lastTime = history.right().getTimestamp();
		double xsum = 0.0;
		double ysum = 0.0;
		int i = history.size() - 1;
		for (; i >= 1 && history.isWithinTimestamp(i, SIGNIFICANT_TIME); i--) {
			xsum += history.get(i).getX();
			ysum += history.get(i).getY();
		}
		double firstTime = history.get(i).getTimestamp();
		
		this.speed = Math.sqrt(xsum * xsum + ysum * ysum) / (lastTime - firstTime);
		uptodate = true;
	}

    /**
     * Returns true if the object is moving
     * @return whether the object is moving
     */
	public boolean isMoving() {
		int i = history.size() - 1;
		
		if (i < 2) {
            return false;
        }
		
		Point last = history.right();
		for (; i >= 0 && history.isWithinTimestamp(i, SIGNIFICANT_TIME); i++) {
			if (last.distance(history.get(i)) > POSITION_EPS) {
                return true;
            }
        }
		return false;
	}
	
}
