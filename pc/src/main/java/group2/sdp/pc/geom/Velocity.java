package group2.sdp.pc.geom;

import java.awt.geom.Point2D;

public class Velocity {

	public double vector;
	public double speed;

	public Velocity(Point2D previousPosition, Point2D currentPosition, double timeDelta) {
		this.vector = currentPosition.distance(previousPosition);
		this.speed = this.vector / timeDelta;
	}

}
