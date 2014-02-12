/**
 * @author Jaroslaw Hirniak, s1143166
 */

package group2.sdp.pc.world;

import group2.sdp.pc.geom.Plane;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.PointSet;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.strategy.Zone;

import java.util.List;

public class Pitch extends Plane implements IPitch {
	
	private double WIDTH = 2165; /** [mm], from goal to goal mouth */
	private double HEIGHT = 1150; /** [mm], from wall to wall */
	
	private Zone[] zones = new Zone[4];
	private Ball ball = new Ball();
	private boolean even;
	private Robot robot;
	
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
	{ super("Pitch"); }

	@Override
	public void setAlly(boolean even)
	{ this.even = even; }

	@Override
	public void updateBallPosition(Point p)
	{ ball.updatePoisition(p); }

	@Override
	public void updateRobotState(byte id, Point p, double theta) {
		//zones[id]
		
	}
	
	public void addBall(Ball ball) {
		this.ball = ball;
	}
	
	public void addRobot(Robot robot) {
		this.robot = robot;
	}
	
	public void updateRobotState(Point robotPosition, Vector direction) {
		this.robot.setPosition(robotPosition);
		this.robot.setDirection(direction);
	}
	
//	public Vector getRobotBallVector() {
//		return this.ball.getPosition().sub(this.robot.getPosition());
//	}
	
//	public Rect getPitchRect(Point rectMember) {
//	       Rect result = new Rect(0.0, 1.0, 0.0, 1.0);
//	       List<Rect> rects = sections.getImportantRects();
//	       for (Rect rect : rects) {
//	    	   if (rect.contains(rectMember)) {
//	    		   result = rect;
//	    	   }
//	        }
//	       return result;
//		}
	
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
}
