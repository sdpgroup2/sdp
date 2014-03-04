/**
 * @author Jaroslaw Hirniak, s1143166
 * @author Mikey
 */

package sdp.group2.world;

import java.util.Collections;
import java.util.List;

import sdp.group2.geometry.*;
import sdp.group2.world.Zone;
import sdp.group2.pc.MasterController;
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
    
    private TeamSide ourSide;

    private Zone[] zones = new Zone[4];
    private Ball ball = new Ball();
    //Specify what Colour and Side of the pitch we are on;
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
    
    private boolean isDefender(Point point) {
		if (zones[0].contains(point) || 
				zones[3].contains(point)) {
			return true;
		}
    	return false;
    }
    
    public void updateRobots(List<Tuple<Point, Point>> robots, TeamColour colour) {
    	if (robots.size() == 2) {
    		Tuple<Point, Point> firstRobot = robots.get(0);
    		Tuple<Point, Point> secondRobot = robots.get(1);
    		Point firstPosition = firstRobot.getFirst();
    		Point firstDotPosition = firstRobot.getSecond();
    		Point secondPosition = secondRobot.getFirst();
    		Point secondDotPosition = secondRobot.getSecond();
    		if (isDefender(firstPosition)) {
    			// First is defender
    			if (colour == TeamColour.YELLOW) {
    				// Team is yellow
    				yellowDefender.updatePosition(firstPosition.toMillis());
    				yellowAttacker.updatePosition(secondPosition.toMillis());
    				if (firstDotPosition != null) {
    					yellowDefender.updateFacing(firstDotPosition.toMillis());
    				}
    				if (secondDotPosition != null) {
    					yellowAttacker.updateFacing(secondDotPosition.toMillis());
    				}
    			} else {
    				// Team is blue
    				blueDefender.updatePosition(firstPosition.toMillis());
    				blueAttacker.updatePosition(secondPosition.toMillis());
    				if (firstDotPosition != null) {
    					blueDefender.updateFacing(firstDotPosition.toMillis());
    				}
    				if (secondDotPosition != null) {
    					blueAttacker.updateFacing(secondDotPosition.toMillis());
    				}
    			}
    		} else {
    			// First is attacker
    			if (colour == TeamColour.YELLOW) {
    				// Team is yellow
    				yellowAttacker.updatePosition(firstPosition.toMillis());
    				yellowDefender.updatePosition(secondPosition.toMillis());
    				if (firstDotPosition != null) {
    					yellowAttacker.updateFacing(firstDotPosition.toMillis());
    				}
    				if (secondDotPosition != null) {
    					yellowDefender.updateFacing(secondDotPosition.toMillis());
    				}
    			} else {
    				blueAttacker.updatePosition(firstPosition.toMillis());
    				blueDefender.updatePosition(secondPosition.toMillis());
    				if (firstDotPosition != null) {
    					blueAttacker.updateFacing(firstDotPosition.toMillis());
    				}
    				if (secondDotPosition != null) {
    					blueDefender.updateFacing(secondDotPosition.toMillis());
    				}
    			}
    		}
    	}
    }

    /**
     * Returns the defender of our team.
     * @return our defender Robot
     */
    public Robot getOurDefenderRobot() {
        if (MasterController.ourTeam == TeamColour.YELLOW) {
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
        if (MasterController.ourTeam == TeamColour.YELLOW) {
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
        if (MasterController.ourTeam == TeamColour.BLUE) {
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
        if (MasterController.ourTeam == TeamColour.BLUE) {
            return yellowAttacker;
        } else {
            return blueAttacker;
        }
    }

    //Zones 0-3

//    // Any returns of -1 is not found
//    public int getYellowAttackZone() {
//        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
//            return 2;
//        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
//            return 1;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
//            return 1;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
//            return 2;
//        } else {
//            return -1;
//        }
//    }
//
//    // Any returns of -1 is not found
//    public int getBlueAttackZone() {
//        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
//            return 1;
//        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
//            return 2;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
//            return 2;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
//            return 1;
//        } else {
//            return -1;
//        }
//    }
//
//    // Any returns of -1 is not found
//    public int getYellowDefendZone() {
//        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
//            return 0;
//        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
//            return 3;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
//            return 3;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
//            return 0;
//        } else {
//            return -1;
//        }
//    }
//
//    // Any returns of -1 is not found
//    public int getBlueDefendZone() {
//        if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.YELLOW) {
//            return 3;
//        } else if (ourSide == TeamSide.LEFT && ourTeam == TeamColour.BLUE) {
//            return 0;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.YELLOW) {
//            return 0;
//        } else if (ourSide == TeamSide.RIGHT && ourTeam == TeamColour.BLUE) {
//            return 3;
//        } else {
//            return -1;
//        }
//    }

    // Any returns of -1 is not found
//    public boolean foeAttackerHasBall() {
//        if (MasterController.ourTeam == TeamColour.YELLOW) {
//            return (getBallZone().getID() == getBlueAttackZone());
//        } else {
//            return (getBallZone().getID() == getYellowAttackZone());
//        }
//    }
//
//    // Any returns of -1 is not found
//    public boolean foeDefenderHasBall() {
//        if (MasterController.ourTeam == TeamColour.YELLOW) {
//            return (getBallZone().getID() == getYellowDefendZone());
//        } else {
//            return (getBallZone().getID() == getBlueDefendZone());
//        }
//    }
//
//    // Any returns of -1 is not found
//    public boolean ourAttackerHasBall() {
//        if (MasterController.ourTeam == TeamColour.YELLOW) {
//            return (getBallZone().getID() == getYellowAttackZone());
//        } else {
//            return (getBallZone().getID() == getBlueAttackZone());
//        }
//    }
//
//    // Any returns of -1 is not found
//    public boolean ourDefenderHasBall() {
//        if (MasterController.ourTeam == TeamColour.YELLOW) {
//            return (getBallZone().getID() == getYellowDefendZone());
//        } else {
//            return (getBallZone().getID() == getBlueDefendZone());
//        }
//    }
    
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
