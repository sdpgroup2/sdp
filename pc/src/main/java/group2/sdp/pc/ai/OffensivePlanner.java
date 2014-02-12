package group2.sdp.pc.ai;

import java.io.IOException;

import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Plane;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.strategy.Robot;
import group2.sdp.pc.strategy.Zone;
import group2.sdp.pc.world.Ball;
import group2.sdp.pc.world.IPitch;

public class OffensivePlanner extends Planner {

	private static final Point GOAL = new Point(0, Plane.pix2mm(70));
	private static final int SPEED = 100;
	
	private Zone offensiveZone;
	private Robot attackerRobot;
	private boolean isRobotAligned = false;
	String robotName = "SDP 2A";
	String robotMacAddress = "CH:UJ";
	private Sender sender;
	
	public OffensivePlanner(IPitch pitch, byte zoneId)
	{ 
		super(pitch);
		this.offensiveZone = getPitch().getZone(zoneId);
		this.attackerRobot = offensiveZone.getRobot();
		
		try { sender = new Sender(robotName, robotMacAddress); }
		catch (IOException e) { e.printStackTrace(); }
	}

	public boolean isAbleToScore()
	{ 
		Point ballPosition = pitch.getBall().getPosition();
		Line arrow = offensiveZone.expand(ballPosition, 0.0);
		Point endpoint = offensiveZone.getIntersection(arrow);
		double space = Math.abs(endpoint.distance(endpoint));
		
		return space >= attackerRobot.getRadius() * 2; 
	}
	
	public void score()
	{
		Point ballPosition = pitch.getBall().getPosition();
		Line line2 = offensiveZone.expand(ballPosition, 0.0);
//		Point endpoint = offensiveZone.getIntersection(arrow);
		Line line1 = line2.getPerpendicular();
	}
	
	public void passBack()
	{
		
	}
	
	public Line getTargetPath()
	{
		Ball ball = pitch.getBall();
		Point ballPosition = ball.getPosition();
		Line arrow = offensiveZone.expand(ballPosition, 0.0);
		arrow.extend(attackerRobot.getRadius() + ball.getRadius());
		Line target = arrow.getPerpendicular();
		return offensiveZone.expand(target);
	}
	
	public void align(double direction)
	{
		double robotDirection = attackerRobot.getDirection();
		double theta = Math.abs(direction - Math.abs(robotDirection)); // rotation to align
		int sign = direction > robotDirection ? 1 : -1;
		int thetaDeg = sign * Zone.rad2deg(theta);
		
		try { sender.rotate(thetaDeg, SPEED); }
		catch (IOException e) { e.printStackTrace(); }
	}
	
	public void act()
	{
		if (attackerRobot.isMoving()) { return; }
		if (isAbleToScore())
		{ score(); }
		else
		{ passBack(); }
	}
	
}
