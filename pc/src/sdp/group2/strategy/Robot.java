package sdp.group2.strategy;

import sdp.group2.geometry.Point;
import sdp.group2.world.MovableObject;

public class Robot extends MovableObject {
	
	private static final double RADIUS = 60.0; /** [mm], measured as robot width from kicker to the back through its centre divided by 2 */
	private double direction = 0.0;
	private Point position = null; 
	
	public double getRadius()
	{ return RADIUS; }
	
	public void setDirection(double direction)
	{
		this.direction = direction;
	}
	
	public double getDirection()
	{
		return direction;
	}
	
	public Point getPosition()
	{ return position; }
	
	public void setPosition(Point p)
	{
		this.position = p;
	}
}
