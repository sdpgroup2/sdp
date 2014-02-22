/**
 * @author Jaroslaw Hirniak, s1143166
 * @author Mikey
 */

package sdp.group2.world;

import sdp.group2.geometry.Plane;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.world.Constants.TeamColour;
import sdp.group2.world.Constants.TeamSide;


public class Pitch extends Plane implements IPitch {
	
	private double WIDTH = 2165; /** [mm], from goal to goal mouth */
	private double HEIGHT = 1150; /** [mm], from wall to wall */
	
	private Zone[] zones = new Zone[4];
	private Ball ball = new Ball();
	private boolean even;
    private static final int CUR_ZONE = 0;

    //Specify what Colour and Side of the pitch we are on;
    private TeamColour ourTeam;
    private TeamSide ourSide;
    
    
    //Can Probably be deleted;
//	private Rect blueAttackZone;
//	private Rect yellowAttackZone;
//	private Rect blueDefenseZone;
//	private Rect yellowDefenseZone;

    private Robot[] robots;

	private Robot blueDefender;
	private Robot blueAttacker;
	private Robot yellowDefender;
	private Robot yellowAttacker;
	

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
	public Pitch(Rect pitchRect, Rect[] sections, TeamColour ourTeam) {
		this();
		this.ourTeam = ourTeam;
		addPoint(new Point(pitchRect.x, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y));
		addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y + pitchRect.height));
		addPoint(new Point(pitchRect.x, pitchRect.y + pitchRect.height));

        byte zoneId;
        
        zoneId = 0;
        
        for (Rect section : sections) {
            zones[zoneId].addPoint(new Point(section.x, section.y));
            zones[zoneId].addPoint(section.x + section.width, section.y);
            zones[zoneId].addPoint(section.x + section.width, section.y + section.height);
            zones[zoneId].addPoint(section.x, section.y + section.height);
            zoneId++;
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
	public Robot getRobot() {
        throw new UnsupportedOperationException("This shouldn't be implemented.");
    }

    
    //Any returns of -1 is not found;
    
	@Override
	public Zone getZone(byte id) {
		return zones[id];
	}

	@Override
	public void setZone(byte id, PointSet ps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBallZone() {
		for (int i = 0; i < zones.length; i++){
			if (zones[i].contains(getBall().getPosition())){
				return i;
			}
		}
		return -1;
	}
	
//	public void setTeamSide(){
//		if (ourTeam == TeamColour.YELLOW &&)
//	}

	@Override
	public PointSet getTrajectory() {
		return super.getTrajectory(ball.getPosition(), ball.getDirection());
	}
	
	@Override
	public Ball getBall() {
        return ball;
    }
	
	
	//Zones 0-3
	
	public int getYellowAttackZone(){
		if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW){
			return 2;
		} else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE){
			return 1;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW){
			return 1;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE){
			return 2;
		} else {
			return -1;
		}
	}
	
	public int getBlueAttackZone(){
		if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW){
			return 1;
		} else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE){
			return 2;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW){
			return 2;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE){
			return 1;
		} else {
			return -1;
		}
	}
	
	public int getYellowDefendZone(){
		if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW){
			return 0;
		} else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE){
			return 3;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW){
			return 3;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE){
			return 0;
		} else {
			return -1;
		}
	}
	
	public int getBlueDefendZone(){
		if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW){
			return 3;
		} else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE){
			return 0;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW){
			return 0;
		} else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE){
			return 3;
		} else {
			return -1;
		}
	}
	
	public boolean foeAttackerHasBall(){
		if (ourTeam == TeamColour.YELLOW){
				return (getBallZone() == getBlueAttackZone());
		} else {
			return (getBallZone() == getYellowAttackZone());
		}
	}
	
	public boolean foeDefenderHasBall(){
		if (ourTeam == TeamColour.YELLOW){
			return (getBallZone() == getYellowDefendZone());
		} else {
			return (getBallZone() == getBlueDefendZone());
		}
	}
	
	public boolean ourAttackerHasBall(){
		if (ourTeam == TeamColour.YELLOW){
			return (getBallZone() == getYellowAttackZone());
		} else {
			return (getBallZone() == getBlueAttackZone());
		}
	}
	
	public boolean ourDefenderHasBall(){
		if (ourTeam == TeamColour.YELLOW){
			return (getBallZone() == getYellowDefendZone());
		} else {
			return (getBallZone() == getBlueDefendZone());
		}
	}
}
