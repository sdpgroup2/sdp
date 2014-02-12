package group2.sdp.pc.ai;

import java.io.IOException;

import lejos.geom.Rectangle;
import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Plane;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.PointSet;
import group2.sdp.pc.strategy.Robot;
import group2.sdp.pc.strategy.Zone;
import group2.sdp.pc.world.IPitch;

public class DefensivePlanner extends Planner {

	private static final int SPEED = 100;
	
	private Zone defenseZone;
	private Robot defenseRobot;
	private boolean isRobotAligned = false;
	String robotName = "SDP2A";
	String robotMacAddress = "00165307D55F";
	private Sender sender;
	
	public DefensivePlanner(IPitch pitch, byte zoneId)
	{ 
		super(pitch);
		this.defenseZone = getPitch().getZone(zoneId);
		this.defenseRobot = pitch.getRobot();
		
		try { sender = new Sender(robotName, robotMacAddress); }
		catch (IOException e) { e.printStackTrace(); }
	}

	public void intercept()
	{
		if (defenseRobot.isMoving())
		{ return; }
		
		align();
		Line trespass = recognizeDanger();
		if (trespass == null) { return; }
		else { System.out.println("Trespassing @ " + trespass.toString()); }
		Line sidewalk = getSidewalk();
		Point intersection = defenseZone.getIntersection(trespass, sidewalk);
		if (intersection == null)
		{
			intersection = new Point(trespass.x1, trespass.y1);
		}
		Point robotPos = defenseRobot.getPosition();
		int sign = robotPos.getY() < intersection.getY() ? 1 : -1;
		int distance = sign * (int) Plane.pix2mm((int) robotPos.distance(intersection));
		
		try { sender.move(sign, SPEED, distance); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void align()
	{
		if (isRobotAligned)
		{ return; }
		
		double direction = Math.PI / 2;
		double robotDirection = defenseRobot.getDirection();
		double theta = Math.abs(Math.abs(direction) - Math.abs(robotDirection)); // rotation to align
		if (theta < 0.1) { isRobotAligned = true; }
		int sign = direction > robotDirection ? 1 : -1;
		int thetaDeg = sign * Zone.rad2deg(theta);
		
		try { sender.rotate(thetaDeg, SPEED); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public Line recognizeDanger()
	{
		PointSet trajectory = getPitch().getTrajectory();
		for (int i = 1; i < trajectory.size() - 1; i++)
		{
			Point p0 = trajectory.get(i - 1);
			Point p1 = trajectory.get(i);
			if (defenseZone.isInterestedByLine(p0, p1))
			{ return new Line(p0, p1); }
		}
		
		return null;
	}
	
	/** Returns the walk path for the robot */
	public Line getSidewalk()
	{
		Rectangle boundary = defenseZone.getBoundary();
		
		Point p0 = new Point(defenseRobot.getPosition().x, boundary.y);
		Point p1 = new Point(defenseRobot.getPosition().y, boundary.width);
		
		return new Line(p0, p1);
	}
	
	public void disconnect() {
		this.sender.disconnect();
	}
	public void act()
	{ intercept(); }
	
}
