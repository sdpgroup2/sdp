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
import sdp.group2.world.Ball;
import sdp.group2.world.IPitch;
import sdp.group2.world.Robot;
import sdp.group2.world.Zone;


public class SimpleDefensivePlanner extends Planner {

    private static final int SPEED = 40;
    private static final String robotName = Constants.ROBOT_2D_NAME;
    private static final Point GOAL = new Point(40, Millimeter.pix2mm(150));
    private boolean isRobotAligned = false;
    private Sender sender;
    private long lastRotation = System.currentTimeMillis();
    private Point enemyGoal = new Point(553, Millimeter.pix2mm(153));

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
    public SimpleDefensivePlanner(IPitch pitch) {
        super(pitch);
    }

    /**
     * TODO: Should check whether we need to rotate
     * Tries to intercept the ball.
     */
    public void interceptSimple() {
    	Robot robot = pitch.getOurDefender();
    	Ball ball = pitch.getBall();
    	System.out.println("Sending intercept comand.");
    	
    	System.out.println(ball.getPosition());
    	System.out.println(robot.getPosition());
        
        int distance = (int) (ball.getPosition().y - robot.getPosition().y);
        int sign = distance < 0 ? -1 : 1;
        distance = Math.abs(distance);
        
        double angle = 90.0 - robot.getDirection();

        System.out.println("Rotating angle: " + angle);
        System.out.println("Moving forward: " + distance);
        
        if (Math.abs(angle) > 10.0) {
            CommandQueue.add(Commands.rotate(((int)Math.floor(angle)), Constants.DEF_MOVE_SPEED), robotName);
        }

        CommandQueue.add(Commands.move(sign, Constants.DEF_MOVE_SPEED, distance), robotName);
//        System.exit(0);
    }

    public void disconnect() {
        this.sender.disconnect();
    }


    //What shall be running when the robot starts
    public void act() {
    	interceptSimple();
    }

}
