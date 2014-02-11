package group2.sdp.pc.geom;

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

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
