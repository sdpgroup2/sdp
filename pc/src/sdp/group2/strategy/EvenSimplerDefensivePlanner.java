package sdp.group2.strategy;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Constants;
import sdp.group2.world.Ball;
import sdp.group2.world.IPitch;
import sdp.group2.world.Robot;
import sdp.group2.util.Debug;

public class EvenSimplerDefensivePlanner extends Planner {
	
	private static final String robotName = Constants.ROBOT_2D_NAME;
	private int frames = 0;
	private int sleepFrames = 0;
	private int currentDirection = 0;
	
	public EvenSimplerDefensivePlanner(IPitch pitch) {
        super(pitch);
    }
	
	public void act() {
		trackBall();
	}
	
	public void trackBall() {
		if (sleepFrames > 0) {
			sleepFrames -= 1;
			return;
		}
		Robot robot = pitch.getOurDefenderRobot();
		Ball ball = pitch.getBall();
		double angle = robot.getFacingVector().signedAngleDegrees();
		double angleSign = Math.signum(angle);
		double distance = robot.getPosition().y - ball.getPosition().y;
		int direction = (int) (Math.signum(distance) * angleSign);
		boolean moving = (currentDirection != 0);
		if (Math.abs(distance) <= 60) {
			Debug.logf("Close enough to ball: %f", distance);
			if (moving) {
				Debug.log("Robot is moving: stopping it.");
				CommandQueue.clear(robotName);
				CommandQueue.add(Commands.stop(), robotName);
				sleepFrames = 30;
				currentDirection = 0;
			}
		} else {
			Debug.logf("Too far from ball: %f", distance);
			CommandQueue.clear(robotName);
			CommandQueue.add(Commands.stop(), robotName);
			moveDirection(direction);
		}
	}
	
	public void moveDirection(int direction) {
		switch (direction) {
		case 1:  {
			CommandQueue.add(Commands.forwards(4000), robotName);
			currentDirection = 1;
			break;
		}
		case -1: {
			CommandQueue.add(Commands.backwards(4000), robotName);
			currentDirection = -1;
			break;
		}
		default: break;
		}
	}
	
	public void stutter() {
		frames += 1;
		Robot robot = pitch.getOurDefenderRobot();
		double angle = robot.getFacingVector().signedAngleDegrees();
		if (frames%60 == 0) {
			Debug.log("Every-three-second rotation!!!");
			Debug.logf("  Defender angle is: %f", angle);
			double unsignedAngle = Math.abs(angle);
			if (85 < unsignedAngle && unsignedAngle < 95) {
				Debug.log("Angle is fine.");
			} else {
				Debug.log("Too big an angle difference, rotating.");
				double angleSign = Math.signum(angle);
				CommandQueue.add(Commands.rotate((int)(angleSign*(90-unsignedAngle)), Short.MAX_VALUE), robotName);
			}
		}
		else if (frames%60 == 30) {
			double ry = robot.getPosition().y;
			double by = pitch.getBall().getPosition().y;
			double angleSign = Math.signum(angle);
			int dir = (int) (Math.signum(ry-by) * angleSign);
			int dist = (int) Math.abs(ry-by);
			if (dist > 30) {
				CommandQueue.clear(robotName);
				CommandQueue.add(Commands.move(dir, 5000, dist), robotName);
			}
		}
	}
	
}
