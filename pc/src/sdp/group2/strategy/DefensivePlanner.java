package sdp.group2.strategy;

import java.io.IOException;

import lejos.geom.Rectangle;
import sdp.group2.comms.Commands;
import sdp.group2.comms.Sender;
import sdp.group2.geometry.Line;
import sdp.group2.geometry.Plane;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.pc.CommandQueue;
import sdp.group2.util.Constants;
import sdp.group2.world.IPitch;
import sdp.group2.world.Robot;
import sdp.group2.world.Zone;


public class DefensivePlanner extends Planner {

    private static final int SPEED = 40;
    private static final String robotName = Constants.ROBOT_2D_NAME;
    private static final Point GOAL = new Point(0, Plane.pix2mm(150));
    private Zone offenceZone;
    private Zone defenceZone;
    private Robot offenceRobot;
    private Robot defenceRobot;
    private boolean isRobotAligned = false;
    private Sender sender;
    private long lastRotation = System.currentTimeMillis();
    private Point enemyGoal;

    // Still to implement:
    // Pass();
    // AbleToPass();
    // Track x Coordinate of Ball;
    // returnToGoal();

    /**
     * Initialises a defensive planner in a given zone for a given pitch
     *
     * @param pitch  pitch we are playing
     * @param zoneId zone the defender is in
     */
    public DefensivePlanner(IPitch pitch, byte zoneId) {
        super(pitch);
        this.defenceZone = getPitch().getZone(zoneId);
        this.defenceRobot = pitch.getOurDefender();
    }

    /**
     * TODO: Should check whether we need to rotate
     * Tries to intercept the ball.
     */
    public void interceptSimple() {
        if (defenceRobot.isMoving()) {
            return;
        }
        double yBall = pitch.getBall().getPosition().getY();
        double yRobot = defenceRobot.getPosition().getY();
        // int distance = (int) Plane.pix2mm((int) (xBall - xRobot));
//        int distance = (int) Plane.pix2mm((int) (yBall - yRobot));
        int distance = (int)GOAL.sub(defenceRobot.getPosition()).length();
        int sign = distance < 0 ? -1 : 1;
        distance = Math.abs(distance);
        
        //ERROR
        double angle = defenceRobot.angleToPoint(new Point (GOAL.x, pitch.getBall().getPosition().y));

        CommandQueue.add(Commands.rotate(((int)Math.floor(angle)), Constants.DEF_MOVE_SPEED), robotName);
        CommandQueue.add(Commands.move(sign, Constants.DEF_MOVE_SPEED, distance), robotName);
    }

    public void intercept() {
        if (defenceRobot.isMoving()) {
            return;
        }

        // align();
        Line trespass = recognizeDangerSimple();
        if (trespass == null) {
            return;
        }
        Line sidewalk = getSidewalk();
        Point intersection = defenceZone.getIntersection(trespass, sidewalk);
        if (intersection == null) {
            return;
        }
        Point robotPos = defenceRobot.getPosition();
        int sign = robotPos.getY() < intersection.getY() ? 1 : -1;
        int distance = sign
                * (int) Plane.pix2mm((int) robotPos.distance(intersection));
        {
            System.out.println("Trespassing @ " + intersection.toString());
        }
        try {
            sender.move(sign, SPEED, distance);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void align() {
        if (lastRotation + 3000 > System.currentTimeMillis()) {
            return;
        } else {
            lastRotation = System.currentTimeMillis();
        }

        double direction = Math.PI / 2;
        double robotDirection = defenceRobot.getDirection();
        double theta = Math.abs(Math.abs(direction) - Math.abs(robotDirection)); // rotation to align
        if (Math.abs(theta) < 0.3) {
            return;
        }
        int sign = direction > robotDirection ? 1 : -1;
        int thetaDeg = sign * Zone.rad2deg(theta);

        try {
            sender.rotate(thetaDeg, SPEED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Unfinished
    public void pass(){
    	//Pass from defender to attacker
    }

    public Line recognizeDangerSimple() {
        return new Line(GOAL, pitch.getBall().getPosition());
    }

    public Line recognizeDanger() {
        PointSet trajectory = getPitch().getTrajectory();
        for (int i = 1; i < trajectory.size() - 1; i++) {
            Point p0 = trajectory.get(i - 1);
            Point p1 = trajectory.get(i);
            if (defenceZone.isInterestedByLine(p0, p1)) {
                return new Line(p0, p1);
            }
        }

        Point p1 = trajectory.left();
        Point p0 = new Point(p1.x, 0.0);
        return new Line(p0, p1);

        // return null;
    }

    /**
     * Returns the walk path for the robot
     *
     * @return
     */
    public Line getSidewalk() {
        Rectangle boundary = defenceZone.getBoundary();

        // Point p0 = new Point(defenceRobot.getPosition().x, boundary.y);
        // Point p1 = new Point(defenceRobot.getPosition().y, boundary.width);

        double x = (boundary.x + boundary.width) / 2.0;
        Point p0 = new Point(x, boundary.y);
        Point p1 = new Point(x, boundary.y + boundary.width);

        return new Line(p0, p1);
    }

    public void disconnect() {
        this.sender.disconnect();
    }


    //What shal be running when the robot starts
    public void act() {
    	//If the ball is in our defending zone, pass;
    	//Else stay at GOAL.x, Ball.y 
    	if (pitch.getBallZone() == pitch.getOurDefendZone()){
    		pass();
    	} else {
    		interceptSimple();
    	}
    }

    //Unfinished
    public void returnToGoal() {

        //Return back to the centre of the goal if ball is either
        //in our attacking zone or enemies defending zone
    }

}
