package sdp.group2.strategy;

import sdp.group2.communication.CommandQueue;
import sdp.group2.util.Constants;
import sdp.group2.util.Debug;
import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;
import sdp.group2.world.Robot;

public class DefensivePlanner extends Planner {
	
//	private static final String robotName = Constants.ROBOT_2D_NAME;
	
	// The number of frames in between commands
//	private int STUTTER_FRAMES = 1;
	
	// The number of frames this has been running.
//	private int frames = 0;
	
	// Test PID
//	private PID pid = new PID(90.0);
	
	public DefensivePlanner(Pitch pitch) {
        super(pitch);
    }
	
	public void act() {
		int ballZoneId = pitch.getBallZone();
		int defenderZoneId = pitch.getOurDefendZone();
		System.out.println("------------------------------------------");
//		System.out.println("Robot position: " + pitch.getOurDefender().getPosition());
//		System.out.println("Ball position: " + pitch.getBall().getPosition());
		if (ballZoneId != defenderZoneId || pitch.getBall().enteringZone(defenderZoneId)) {
			defend();
		} else {
			pass();
		}
		System.out.println("------------------------------------------");
	}
	
	public void pass() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();

		System.out.println("Kicker: " + robot.isKickerOpen());
		System.out.println("Distance to ball: " + robot.distanceTo(ball));
		System.out.println("Angle to ball: " + robot.angleTo(ball));
		System.out.println("Has ball: " + robot.hasBall(ball));
		
		if (!robot.isKickerOpen() && !robot.hasBall(ball)) {
			robot.openKicker();
			return;
			
		} else if (robot.hasBall(ball) && !robot.isKickerOpen()) {
			// Angle ranges from -180 to 180 degrees.
			if (robot.inCenter(getPitch())) {
				if (robot.shouldPassAlign()) {
					robot.passAlign();	
					return;
				} else {
					robot.kick();
					return;
				}
			} else {
				robot.alignWith(pitch.getCenter(), 100);
				return;
			}
		}
		
		if (!ball.isStable() && !robot.canGrab(ball)) {
			return;
		}
		
		if (robot.canGrab(ball) && robot.isKickerOpen()) {
			robot.closeKicker();
			return;
		}
		
		// Angle ranges from -180 to 180 degrees.
		double angle = robot.angleTo(ball);
		double unsignedAngle = Math.abs(angle);
		
		// The angle is wrong if it is more than 10 degrees away from 90.
		boolean wrongAngle = !(unsignedAngle < 20);
		System.out.println("Angle to ball:" + angle);
		System.out.println("Distance to ball:" + robot.distanceTo(ball));
		System.out.println("Ball position:" + ball.getPosition());
		System.out.println("Robot position:" + robot.getPosition());
		
		if (wrongAngle) {
	    	robot.rotate(- 0.7 * angle);
		} else {
			int dist = (int) robot.distanceTo(ball);
			// Multiply by 0.9 so that we don't hit the wall and stuff
			dist = (int) (0.5 * dist);
			
			// If the distance is too great, and the robot is roughly vertically aligned:
			robot.forward(1, dist, 100);
		}
		
	}
	
	/**
	 * Do this when we don't have the ball
	**/
	public void defend() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();
		
		if (robot.shouldDefenceAlign()) {
			robot.defenceAlign();
			return;
		}
		
		CommandQueue.clear(Constants.ROBOT_2D_NAME);
		robot.alignWith(ball, 1000);
	}
}
