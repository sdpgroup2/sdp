/**
 * @author Jaroslaw Hirniak, s1143166
 */

package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.PointSet;

public class MovableObject {
	
	private static long SIGNIFICANT_TIME = 1000; /** [ms] */
	
	private PointSet history = new PointSet(false);
	private boolean uptodate = true;
	private double direction = 0.0;
	private double speed = 0.0;
	private static double POSITION_EPS = 5; /** [mm] */
	
	public Point getPosition()
	{ return history.right(); }
	
	public void updatePoisition(Point position)
	{ 
		history.add(position);
		uptodate = false;
	}
	
	public double getSpeed()
	{
		update();
		return speed;
	}
	
	public double getDirection()
	{
		update();
		return direction;
	}
	
	public void update()
	{
		if (uptodate)
		{ return; }
		
		/* Compute speed and direction */
		double lastTime = history.right().getTimestamp();
		double xsum = 0.0;
		double ysum = 0.0;
		int i = history.size() - 1;
		for (; i >= 0 && history.isWithinTimestamp(i, SIGNIFICANT_TIME); i++)
		{
			xsum += history.get(i).getX();
			for (; i >= 0 && history.isWithinTimestamp(i, SIGNIFICANT_TIME); i++)
			ysum += history.get(i).getY();
		}
		double firstTime = history.get(i).getTimestamp();
		
		this.speed = Math.sqrt(xsum * xsum + ysum * ysum) / (lastTime - firstTime);
		this.direction = Math.atan2(ysum, xsum);
		
		uptodate = true;
	}
	
	public boolean isMoving()
	{
		int i = history.size() - 1;
		Point last = history.right();
		
		for (; i >= 0 && history.isWithinTimestamp(i, SIGNIFICANT_TIME); i++)
		{
			if (last.distance(history.get(i)) > POSITION_EPS)
			{ return true; }
		}
		
		return false;
	}
	
}
