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


public class Pitch extends Plane {
	
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
    private Ball ball;
    //Specify what Colour and Side of the pitch we are on;

    private Robot blueDefender;
    private Robot blueAttacker;
    private Robot yellowDefender;
    private Robot yellowAttacker;


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

    public Zone getZone(int id) {
        return zones[id];
    }

    public void updateBallPosition(Point p) {
    	ball.updatePosition(p);
    }
    
    public void updateBallPosition(double x, double y) {
    	updateBallPosition(new Point(x, y));
    }

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

//    @Override
//    public PointSet getTrajectory() {
//        return super.getTrajectory(ball.getPosition(), ball.getDirection());
//    }

    public Ball getBall() {
        return ball;
    }
    
    public void updateRobots(List<Tuple<Point, Point>> yellowRobots, List<Tuple<Point, Point>> blueRobots) {
    	// We find out which one is left from history
    	// TODO: Add zone assignments to robots
    	if (yellowDefender.getPosition().x < blueDefender.getPosition().x) {
    		// Yellow | Blue | Yellow | Blue
    		if (yellowRobots.size() == 2) {
    			// Sort them by X position
    			Collections.sort(yellowRobots);
    			// Defender is 0
    			Tuple<Point, Point> tuple = yellowRobots.get(0);
    			yellowDefender.updateState(tuple);
    			// Attacker is 1
    			tuple = yellowRobots.get(1);
    			yellowAttacker.updateState(tuple);
        	}
    		// Yellow | Blue | Yellow | Blue
    		if (blueRobots.size() == 2) {
    			// Sort them by X position
    			Collections.sort(blueRobots); 
    			// Attacker is 0
    			Tuple<Point, Point> tuple = blueRobots.get(0);
    			blueAttacker.updateState(tuple);
    			// Defender is 1
    			tuple = blueRobots.get(1);
    			blueDefender.updateState(tuple);
    		}
    	} else {
    		// Blue | Yellow | Blue | Yellow
    		if (yellowRobots.size() == 2) {
    			// Sort them
    			Collections.sort(yellowRobots);
    			// Attacker is 0
    			Tuple<Point, Point> tuple = yellowRobots.get(0);
    			yellowAttacker.updateState(tuple);
    			// Defender is 1
    			tuple = yellowRobots.get(1);
    			yellowDefender.updateState(tuple);
        	}
    		// Blue | Yellow | Blue | Yellow
    		if (blueRobots.size() == 2) {
    			// Sort them
    			Collections.sort(blueRobots);
    			// Defender is 0
    			Tuple<Point, Point> tuple = blueRobots.get(0);
    			blueDefender.updateState(tuple);
    			// Attacker is 1
    			tuple = blueRobots.get(1);
    			blueAttacker.updateState(tuple);
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
    	if (MasterController.ourTeam == TeamColour.YELLOW) {
    		return yellowDefender.getZone();
    	} else {
    		return blueDefender.getZone();
    	}
    }
    
    public Zone getOurAttackZone(){
    	if (MasterController.ourTeam == TeamColour.YELLOW) {
    		return yellowAttacker.getZone();
    	} else {
    		return blueAttacker.getZone();
    	}
    }

	public void setZone(int id, PointSet ps) {
		
	}

	public Zone[] getAllZoneOutline() {
		return zones;
	}

	public void initialise(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots) {
		
		// We have to be prepared at this point so cannot be null
		this.ball = new Ball(ballCentroid);
		
		// We sort both
		Collections.sort(yellowRobots);
		Collections.sort(blueRobots);
		
		// We compare first one - smaller should be left
		Tuple<Point, Point> firstYellow = yellowRobots.get(0);
		Tuple<Point, Point> firstBlue = blueRobots.get(0);
		Tuple<Point, Point> secondYellow = yellowRobots.get(1);
		Tuple<Point, Point> secondBlue = blueRobots.get(1);
		
		if (firstYellow.getFirst().x < firstBlue.getFirst().x) {
    		// Yellow | Blue | Yellow | Blue
			// Yellow is on the left of blue
			// Thus yellow 0 is defender and yellow 1 is attacker
			// And blue 0 is attacker and blue 1 is defender
			yellowDefender = new Robot(firstYellow.getFirst(), firstYellow.getSecond(),zones[0]);
			yellowAttacker = new Robot(secondYellow.getFirst(), secondYellow.getSecond(),zones[2]);
			blueAttacker = new Robot(firstBlue.getFirst(), firstBlue.getSecond(),zones[1]);
			blueDefender = new Robot(secondBlue.getFirst(), secondBlue.getSecond(),zones[3]);
		} else {
    		// Blue | Yellow | Blue | Yellow
			// Blue is on the left of yellow
			// Thus yellow 0 is attacker and yellow 1 is defender
			// And blue 0 is defender and blue 1 is attacker
			yellowAttacker = new Robot(firstYellow.getFirst(), firstYellow.getSecond(),zones[1]);
			yellowDefender = new Robot(secondYellow.getFirst(), secondYellow.getSecond(),zones[3]);
			blueDefender = new Robot(firstBlue.getFirst(), firstBlue.getSecond(),zones[0]);
			blueAttacker = new Robot(secondBlue.getFirst(), secondBlue.getSecond(),zones[2]);
		}
		
	}

	public void updateRobotState(int id, Point p, double theta) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public PointSet getTrajectory() {
		throw new UnsupportedOperationException();
	}
}
