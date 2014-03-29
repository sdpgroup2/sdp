package sdp.group2.geometry;

import javax.vecmath.Vector2d;

public class Vector extends Vector2d {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Vector(double x, double y) {
        super(x, y);
    }

    /**
     * Calculates the angle between this and the other vector (Tested).
     * @param other the other vector
     * @return angle in degrees
     */
	public double angleDegrees(Vector other) {
        return Math.toDegrees(Math.atan2(this.y, this.x) - Math.atan2(other.y, other.x));
	}
	
	/**
	 * Reverses a vector (not in place). Maybe
	 * someone knows a better way to do this.
	 * @return new Vector object with reversed values
	 */
	public Vector reverse() {
		Vector newVector = (Vector) this.clone();
		newVector.negate();
		return newVector;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
