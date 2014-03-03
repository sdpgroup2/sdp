/**
 * @author Jaroslaw Hirniak, s1143166
 * @author Mikey
 */

package sdp.group2.world;

import java.util.Collections;
import java.util.List;

import sdp.group2.geometry.*;
import sdp.group2.world.Zone;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.util.Constants.TeamSide;
import sdp.group2.util.Tuple;


public class Pitch extends Plane implements IPitch {

	private static Pitch instance = null;
	
    private static final int CUR_ZONE = 0;
    private double WIDTH = 2165;
    /**
     * [mm], from goal to goal mouth
     */
    private double HEIGHT = 1150;
    /**
     * [mm], from wall to wall
     */

    private Zone[] zones = new Zone[4];
    private Ball ball = new Ball();
    //Specify what Colour and Side of the pitch we are on;
    private TeamColour ourTeam;
    private TeamSide ourSide;

    private Robot[] robots;

    private Robot blueDefender = new Robot();
    private Robot blueAttacker = new Robot();
    private Robot yellowDefender = new Robot();
    private Robot yellowAttacker = new Robot();


    /**
     * Initialises the pitch
     */
    public Pitch() {
        super("Pitch");
        for (int i = 0; i < zones.length; i++) {
            zones[i] = new Zone(i);
        }
    }

    /**
     * Initialises the pitch with a given bounding rectangle of the pitch
     * and the sections.
     *
     * @param pitchRect bounding rectangle of the pitch
     * @param sections  bounding rectangles of the sections
     */
    public Pitch(Rect pitchRect, Rect[] sections, TeamColour ourTeam) {
        this();
        this.ourTeam = ourTeam;
        addPoint(new Point(pitchRect.x, pitchRect.y));
        addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y));
        addPoint(new Point(pitchRect.x + pitchRect.width, pitchRect.y + pitchRect.height));
        addPoint(new Point(pitchRect.x, pitchRect.y + pitchRect.height));

        int zoneId = 0;
        for (Rect section : sections) {
            zones[zoneId].addPoint(new Point(section.x, section.y));
            zones[zoneId].addPoint(section.x + section.width, section.y);
            zones[zoneId].addPoint(section.x + section.width, section.y + section.height);
            zones[zoneId].addPoint(section.x, section.y + section.height);
            zoneId++;
        }
    }
    
    public Pitch(PointSet pitchConvexHull, PointSet[] zoneConvexHulls) {
    	this();
    	
    	setOutline(pitchConvexHull);
    	
    	for (int i = 0; i < zones.length; i++) {
    		zones[i].setOutline(zoneConvexHulls[i]);
    	}
    
    }

    public void addBall(Ball ball) {
        this.ball = ball;
    }

    public void updateRobotStates(Point robotPosition, Vector direction) {
        //this.robot.setPosition(robotPosition);
        //this.robot.setDirection(direction.angle(new Vector2d(robotPosition.x, robotPosition.y)));
    }

    public void setZoneOutline(int id, PointSet ps) {
        zones[id].setOutline(ps);
    }
    
    public void setAllZonesOutlines(PointSet[] outlines) {
    	if (outlines.length != zones.length) {
    		throw new IllegalArgumentException("Expected the number of outlines (" + outlines.length + 
    				") to be equal to the number of zones (" + zones.length  + "), but was not.");
    	}
    	for (int i = 0; i < zones.length; i++) {
    		zones[i].setOutline(outlines[i]);
    	}
    }

//	public Vector getRobotBallVector() {
//		return this.ball.getPosition().sub(this.robot.getPosition());
//	}

    @Override
    public Zone getZone(int id) {
        return zones[id];
    }

    @Override
    public void updateBallPosition(Point p) {
    	ball.updatePosition(p);
    }
    
    public void updateBallPosition(double x, double y) {
    	updateBallPosition(new Point(x, y));
    }

    @Override
    public void updateRobotState(int id, Point p, double theta) {
        zones[id].updateRobotState(p, theta);
    }

    @Override
    public Zone getBallZone() {
        for (int i = 0; i < zones.length; i++) {
            if (zones[i].contains(getBall().getPosition())) {
                return new Zone((int) i);
            }
        }
        return new Zone((int) -1);
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
    
    public void updateRobots(List<Tuple<Point, Point>> robots, TeamColour colour) {
    	// Only take more than 1 robot because otherwise
    	// we don't know if defender or attacker
    	if (robots.size() > 1) {
    		// Needs to be sorted by defender first and attacker next or something
    		Collections.sort(robots);
    		Robot defender;
    		Robot attacker;
    		
    		if (colour == TeamColour.YELLOW) {
    			defender = yellowDefender;
    			attacker = yellowAttacker;
    		} else {
    			defender = blueDefender;
    			attacker = blueAttacker;
    		}
    		
    		Tuple<Point, Point> tuple = robots.get(0);
			defender.updatePosition(tuple.getFirst().toMillis());
    		Point dotCenter = tuple.getSecond();
			if (dotCenter == null) {
				defender.updateFacing(dotCenter);
			}
			
			tuple = robots.get(1);
			attacker.updatePosition(tuple.getFirst().toMillis());
			dotCenter = tuple.getSecond();
			if (dotCenter == null) {
				attacker.updateFacing(dotCenter);
			}
    	}
    }

    /**
     * Returns the defender of our team.
     * @return our defender Robot
     */
    public Robot getOurDefenderRobot() {
        if (ourTeam == TeamColour.YELLOW) {
            return yellowDefender;
        } else {
            return blueDefender;
        }
    }

    /**
     * Returns the attacker of our team.
     * @return our attacker Robot
     */
    public Robot getOurAttacker() {
        if (ourTeam == TeamColour.YELLOW) {
            return yellowAttacker;
        } else {
            return blueAttacker;
        }
    }

    /**
     * Returns the defender of the opposing team.
     * @return opposing defender Robot
     */
    public Robot getFoeDefender() {
        if (ourTeam == TeamColour.BLUE) {
            return yellowDefender;
        } else {
            return blueDefender;
        }
    }

    /**
     * Returns the attacker of the opposing team.
     * @return opposing attacker Robot
     */
    public Robot getFoeAttacker() {
        if (ourTeam == TeamColour.BLUE) {
            return yellowAttacker;
        } else {
            return blueAttacker;
        }
    }

    //Zones 0-3

    // Any returns of -1 is not found
    public int getYellowAttackZone() {
        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
            return 2;
        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
            return 1;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
            return 1;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
            return 2;
        } else {
            return -1;
        }
    }

    // Any returns of -1 is not found
    public int getBlueAttackZone() {
        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
            return 1;
        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
            return 2;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
            return 2;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
            return 1;
        } else {
            return -1;
        }
    }

    // Any returns of -1 is not found
    public int getYellowDefendZone() {
        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
            return 0;
        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
            return 3;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
            return 3;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
            return 0;
        } else {
            return -1;
        }
    }

    // Any returns of -1 is not found
    public int getBlueDefendZone() {
        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
            return 3;
        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
            return 0;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
            return 0;
        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
            return 3;
        } else {
            return -1;
        }
    }

    // Any returns of -1 is not found
    public boolean foeAttackerHasBall() {
        if (ourTeam == TeamColour.YELLOW) {
            return (getBallZone().getID() == getBlueAttackZone());
        } else {
            return (getBallZone().getID() == getYellowAttackZone());
        }
    }

    // Any returns of -1 is not found
    public boolean foeDefenderHasBall() {
        if (ourTeam == TeamColour.YELLOW) {
            return (getBallZone().getID() == getYellowDefendZone());
        } else {
            return (getBallZone().getID() == getBlueDefendZone());
        }
    }

    // Any returns of -1 is not found
    public boolean ourAttackerHasBall() {
        if (ourTeam == TeamColour.YELLOW) {
            return (getBallZone().getID() == getYellowAttackZone());
        } else {
            return (getBallZone().getID() == getBlueAttackZone());
        }
    }

    // Any returns of -1 is not found
    public boolean ourDefenderHasBall() {
        if (ourTeam == TeamColour.YELLOW) {
            return (getBallZone().getID() == getYellowDefendZone());
        } else {
            return (getBallZone().getID() == getBlueDefendZone());
        }
    }
    
    public Zone getOurDefendZone(){
    	if (ourSide == TeamSide.LEFT){
    		return new Zone(0);
    	} else {
    		return new Zone(3);
    	}
    }
    
    public Zone getOurAttackZone(){
    	if (ourSide == TeamSide.LEFT){
    		return new Zone(2);
    	} else {
    		return new Zone(1);
    	}
    }

	@Override
	public void setZone(int id, PointSet ps) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Zone[] getAllZoneOutline() {
		return zones;
	}
}
