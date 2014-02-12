package group2.sdp.pc.ai;

import java.io.IOException;

import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Plane;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.strategy.Robot;
import group2.sdp.pc.strategy.Zone;
import group2.sdp.pc.world.IPitch;

public class OffensivePlanner extends Planner {

	private static final Point GOAL = new Point(0, Plane.pix2mm(70));
	
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
		Point ballPosition = pitch.getBallPosition();
		Line arrow = offensiveZone.expand(ballPosition, 0.0);
		Point endpoint = offensiveZone.getIntersection(arrow);
		double space = Math.abs(endpoint.distance(endpoint));
		
		return space >= attackerRobot.getRadius() * 2; 
	}
	
}
