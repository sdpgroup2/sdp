package sdp.group2.geometry;

import javax.vecmath.Vector2d;

public class Vector extends Vector2d {

	private static final long serialVersionUID = 1L;

	public Vector(double d, double e) {
		super(d, e);
	}
	
	public void averageWith(Vector other) {
		this.x = (this.x + other.x) / 2;
		this.y = (this.y + other.y) / 2;
	}
	
	public double angleDegrees(Vector other) {
		return Math.toDegrees(this.angle(other));
	}
	
	/**
	 * Returns the angle in degrees relative to positive x axis.
	 * @return angle in degrees
	 */
	public double signedAngleDegrees() {
		return Math.toDegrees(Math.atan2(this.y, this.x));
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
