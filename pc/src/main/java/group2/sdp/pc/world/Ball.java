package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

public class Ball extends CircularObjectAdapter implements MovingObject {

	private Point previousPosition;

	public Ball(Rect boundingRect) {
		super(boundingRect);
		this.previousPosition = boundingRect.getCenter();
	}
	
//	public double getSpeed()
//	{
//		update();
//		return speed;
//	}

	@Override
	public Vector getVelocity() {
		return this.position.sub(previousPosition);
	}

	@Override
	public boolean isNearWall(PitchM pitch, double distance) {
		return false;
	}
	
//	public void update()
//	{
//		if (uptodate)
//		{ return; }
//		
//		/* Compute speed and direction */
//		double lastTime = history.right().getTimestamp();
//		double xsum = 0.0;
//		double ysum = 0.0;
//		int i = history.size() - 1;
//		for (; i >= 0 && history.isWithinTimestamp(i, significantTime); i++)
//		{
//			xsum += history.get(i).getX();
//			ysum += history.get(i).getY();
//		}
//		double firstTime = history.get(i).getTimestamp();
//		
//		this.speed = Math.sqrt(xsum * xsum + ysum * ysum) / (lastTime - firstTime);
//		this.direction = Math.atan2(ysum, xsum);
//		
//		uptodate = true;
//	}
	
}
