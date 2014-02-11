package group2.sdp.pc.geom;

import javax.vecmath.Vector2d;

public class Vector extends Vector2d {

	private static final long serialVersionUID = 1L;


	public Vector(double d, double e) {
		super(d, e);
	}


	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
