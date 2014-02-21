/**
 * @author Jaroslaw Hirniak, s1143166
 */

package sdp.group2.world;

import javax.vecmath.Vector2d;

import sdp.group2.geometry.Plane;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.strategy.Robot;
import sdp.group2.strategy.Zone;

public class Pitch extends Plane implements IPitch {
	
	private double WIDTH = 2165; /** [mm], from goal to goal mouth */
	private double HEIGHT = 1150; /** [mm], from wall to wall */
	
	private Zone[] zones = new Zone[4];
	private Ball ball = new Ball();
	private boolean even;
	private byte CUR_ZONE = 0;

/*	public Rect getPitchRect() {
		return pitchRect;
	}

	public void setPitchRect(Rect pitchRect) {
		this.pitchRect = pitchRect;
	}

	private Rect pitchRect;
	private Rect[] sections;

	private Rect leftAttackZone;
	private Rect rightAttackZone;
	private Rect leftDefenseZone;
	private Rect rightDefenseZone;

//	private Robot leftDefender;
//	private Robot leftAttacker;
//	private Robot rightDefender;
//	private Robot rightAttacker;

	// Milestone 3 - only one robot needed
*/	
	private Robot robot = new Robot();
	
	// Points that should definitely belong to respective zones
	private final Point LEFT_DEF_POINT = new Point(120, 240);
	private final Point LEFT_ATT_POINT = new Point(270, 240);
	private final Point RIGHT_DEF_POINT = new Point(370, 240);
	private final Point RIGHT_ATT_POINT = new Point(410, 240);
//
//	public Pitch(PitchLinesCluster lines, PitchSectionCluster sections) {
//		this.lines = lines;
//		this.sections = sections;
//
////		pitchRect = lines.getImportantRects().get(0);
////		leftDefenseZone = getPitchRect(LEFT_DEF_POINT);
////		leftAttackZone = getPitchRect(LEFT_ATT_POINT);
////		rightDefenseZone = getPitchRect(RIGHT_DEF_POINT);
////		rightAttackZone = getPitchRect(RIGHT_ATT_POINT);
//		
//	}
	
	public Pitch()
	{ 
		super("Pitch");
		for (byte i = 0; i < zones.length; i++)
		{ zones[i] = new Zone(i); }
	}

	@Override
	public void setAlly(boolean even)
	{ this.even = even; }

	public Pitch(Rect pitchRect, Rect[] sections) {
		this();
		
		// TODO : parse
		// this.pitchRect = pitchRect;
		addPoint(new Point(pitchRect.x, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y + pitchRect.height));
		addPoint(new Point(pitchRect.x, pitchRect.y + pitchRect.height));
		
		// this.sections = sections;
		byte id = 0;
		for (Rect section : sections) {
			if (section.contains(LEFT_DEF_POINT)) {
				zones[0].addPoint(new Point(section.x, section.y));
				zones[0].addPoint(section.x + section.width, section.y);
				zones[0].addPoint(section.x + section.width, section.y + section.height);
				zones[0].addPoint(section.x, section.y + section.height);
			} else {
				zones[id].addPoint(new Point(section.x, section.y));
				zones[id].addPoint(section.x + section.width, section.y);
				zones[id].addPoint(section.x + section.width, section.y + section.height);
				zones[id].addPoint(section.x, section.y + section.height);
			}
			id++;
		}
	}

	@Override
	public void updateBallPosition(Point p)
	{ ball.updatePosition(p); }

	@Override
	public void updateRobotState(byte id, Point p, double theta) {
		//zones[id]
		zones[CUR_ZONE].updateRobotState(id, p, theta);
	}
	
	public void addBall(Ball ball) {
		this.ball = ball;
	}
	
	public void addRobot(Robot robot) throws Exception {
		// this.robot = robot;
		throw new Exception("Not to be used");
	}
	
	public void updateRobotState(Point robotPosition, Vector direction) {
		this.robot.setPosition(robotPosition);
		this.robot.setDirection(direction.angle(new Vector2d(robotPosition.x, robotPosition.y)));
	}
	
//	public Vector getRobotBallVector() {
//		return this.ball.getPosition().sub(this.robot.getPosition());
//	}
	
	public Robot getRobot() { 
		return this.robot;
	}

	@Override
	public Zone getZone(byte id) {
		return zones[id];
	}

	@Override
	public void setZone(byte id, PointSet ps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IPitch getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte getBallZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAllyEven() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTachoToPixelValue(double ratio) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getTachoToPixelRatio() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setToTachoTranslation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetToTachoTranslation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PointSet getTrajectory() {
		return super.getTrajectory(ball.getPosition(), ball.getDirection());
	}
	
	public Ball getBall()
	{ return ball; }
}
