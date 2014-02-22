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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Pitch extends Plane implements IPitch {
	
	private double WIDTH = 2165; /** [mm], from goal to goal mouth */
	private double HEIGHT = 1150; /** [mm], from wall to wall */
	
	private Zone[] zones = new Zone[4];
	private Ball ball = new Ball();
	private boolean even;
    private static final int CUR_ZONE = 0;

	private Rect leftAttackZone;
	private Rect rightAttackZone;
	private Rect leftDefenseZone;
	private Rect rightDefenseZone;

    private Robot[] robots;

	private Robot leftDefender;
	private Robot leftAttacker;
	private Robot rightDefender;
	private Robot rightAttacker;
	
	// Points that should definitely belong to respective zones
	private final Point LEFT_DEF_POINT = new Point(120, 240);
	private final Point LEFT_ATT_POINT = new Point(270, 240);
	private final Point RIGHT_DEF_POINT = new Point(370, 240);
	private final Point RIGHT_ATT_POINT = new Point(410, 240);

    /**
     * Initialises the pitch
     */
	public Pitch() {
		super("Pitch");
		for (byte i = 0; i < zones.length; i++) {
            zones[i] = new Zone(i);
        }
	}

    /**
     * Initialises the pitch with a given bounding rectangle of the pitch
     * and the sections.
     * @param pitchRect bounding rectangle of the pitch
     * @param sections bounding rectangles of the sections
     */
	public Pitch(Rect pitchRect, Rect[] sections) {
		this();
		addPoint(new Point(pitchRect.x, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y + pitchRect.height));
		addPoint(new Point(pitchRect.x, pitchRect.y + pitchRect.height));

        byte zoneId;
        for (Rect section : sections) {
			if (section.contains(LEFT_DEF_POINT)) {
                zoneId = 0;
			} else if (section.contains(RIGHT_ATT_POINT)) {
                zoneId = 1;
			} else if (section.contains(LEFT_ATT_POINT)) {
                zoneId = 2;
            } else {
                zoneId = 3;
            }
            zones[zoneId].addPoint(new Point(section.x, section.y));
            zones[zoneId].addPoint(section.x + section.width, section.y);
            zones[zoneId].addPoint(section.x + section.width, section.y + section.height);
            zones[zoneId].addPoint(section.x, section.y + section.height);
		}
	}

	@Override
	public void updateBallPosition(Point p){
        ball.updatePosition(p);
    }

	@Override
	public void updateRobotState(byte id, Point p, double theta) {
		//zones[id]
		zones[CUR_ZONE].updateRobotState(id, p, theta);
	}
	
	public void addBall(Ball ball) {
		this.ball = ball;
	}
	
	public void updateRobotStates(Point robotPosition, Vector direction) {
		//this.robot.setPosition(robotPosition);
		//this.robot.setDirection(direction.angle(new Vector2d(robotPosition.x, robotPosition.y)));
	}
	
//	public Vector getRobotBallVector() {
//		return this.ball.getPosition().sub(this.robot.getPosition());
//	}

	@Override
	public Zone getZone(byte id) {
		return zones[id];
	}

	@Override
	public void setZone(byte id, PointSet ps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte getBallZone() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PointSet getTrajectory() {
		return super.getTrajectory(ball.getPosition(), ball.getDirection());
	}
	
	public Ball getBall() {
        return ball;
    }
}
