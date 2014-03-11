package sdp.group2.strategy;

import java.io.IOException;


import lejos.geom.Rectangle;
import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.communication.Sender;
import sdp.group2.geometry.Line;
import sdp.group2.geometry.Millimeter;
import sdp.group2.geometry.Plane;
import sdp.group2.geometry.Point;
import sdp.group2.geometry.PointSet;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Constants;
import sdp.group2.world.*;


public class DefensivePlanner extends Planner {

    private static final int SPEED = 40;
    private static final String robotName = Constants.ROBOT_2D_NAME;
    private static final Point GOAL = new Point(0, Millimeter.pix2mm(150));
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
    public DefensivePlanner(Pitch pitch) {
        super(pitch);
    }

    /**
     * TODO: Should check whether we need to rotate
     * Tries to intercept the ball.
     */
    public void interceptSimple() {
    	Robot defenceRobot = pitch.getOurDefenderRobot();
    	Ball ball = pitch.getBall();
    	System.out.println("Sending intercept comand.");
        if (defenceRobot.isMoving()) {
            return;
        }
        Vector diffVector = ball.getPosition().sub(defenceRobot.getPosition());
        int distance = (int) Math.round(diffVector.y);
        int sign = distance < 0 ? -1 : 1;
        distance = Math.abs(distance);
        
        //ERROR
        double angle = defenceRobot.angleToPoint(new Point(GOAL.x, ball.getPosition().y));

        CommandQueue.add(Commands.rotate(((int)Math.floor(angle)), Constants.DEF_MOVE_SPEED), robotName);
        CommandQueue.add(Commands.move(sign, Constants.DEF_MOVE_SPEED, distance), robotName);
    }

    public void intercept() {
    	Robot defenceRobot = pitch.getOurDefenderRobot();
    	Zone defenceZone = pitch.getOurDefendZone();
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
    	Robot defenceRobot = pitch.getOurDefenderRobot();
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
    	System.out.println("Sending pass command.");
    }

    public Line recognizeDangerSimple() {
        return new Line(GOAL, pitch.getBall().getPosition());
    }

    public Line recognizeDanger() {
    	Zone defenceZone = pitch.getOurDefendZone();
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
    	Zone defenceZone = pitch.getOurDefendZone();
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


    //What shall be running when the robot starts
    public void act() {
    	Zone defenceZone = pitch.getOurDefendZone();
    	//If the ball is in our defending zone, pass;
    	//Else stay at GOAL.x, Ball.y 
    	if (pitch.getBallZone().getID() == defenceZone.getID()){
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
