package sdp.group2.strategy;

import sdp.group2.communication.CommandQueue;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;
import sdp.group2.world.Robot;

public class DefensivePlanner extends Planner {
	
	private static final String robotName = Constants.ROBOT_2D_NAME;
	
	// The number of frames in between commands
	private int STUTTER_FRAMES = 1;
	
	// The number of frames this has been running.
	private int frames = 0;
	
	// Test PID
	private PID pid = new PID(90.0);	
	
	public DefensivePlanner(Pitch pitch) {
        super(pitch);
    }
	
	public void act() {
		frames += 1;
		int ballZoneId = pitch.getBallZone();
		int defenderZoneId = pitch.getOurDefendZone();
		
		if (ballZoneId != defenderZoneId) {
//			defend();
		} else {
			pass();
		}
	}
	
	public void pass() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();

		System.out.println("Kicker: " + robot.isKickerOpen());
		if (!robot.isKickerOpen() && !robot.hasBall(ball)) {
			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			robot.openKicker();
			System.out.println("Opened the kicker!");
			
		} else if (robot.hasBall(ball)) {
			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			// Angle ranges from -180 to 180 degrees.
			if (MasterController.pitchPlayed == PitchType.MAIN) {
				robot.alignWith(Constants.PITCH0_CENTER);
			} else {
				robot.alignWith(Constants.PITCH1_CENTER);	
			}
			robot.passAllign();
			System.out.println("Aligned for passing!");
			robot.kick();
			System.out.println("Kicked!");
			return;
		}
		System.out.println("Don't have the ball!");
		
		if (!ball.isStable() && !robot.canGrab(ball)) {
			return;
		}
		System.out.println("Ball is stable!");
		
		if (robot.canGrab(ball) && robot.isKickerOpen()) {
			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			robot.closeKicker();
			return;
		}
		
		// Angle ranges from -180 to 180 degrees.
		double angle = robot.angleTo(ball);
		double unsignedAngle = Math.abs(angle);
		
		// The angle is wrong if it is more than 10 degrees away from 90.
		boolean wrongAngle = !(unsignedAngle < 20);
		
		if (wrongAngle) {
			System.out.printf("Rotate by: %f.2\n", angle);
	    	robot.rotate(- 0.7 * angle);
		} else {
			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			int dist = (int) robot.distanceTo(ball);
			// Multiply by 0.9 so that we don't hit the wall and stuff
			dist = (int) (0.5 * dist);
			
			// If the distance is too great, and the robot is roughly vertically aligned:
			System.out.printf("Going %d mms\n", dist);
			robot.forward(1, dist, 100);
			System.out.println("Went to the ball!");
		}
		
	}
	
	/**
	 * Do this when we don't have the ball
	**/
	public void defend() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();
		
		if (robot.shouldAlign()) {
			robot.defenceAlign();
			return;
		}
		
		CommandQueue.clear(Constants.ROBOT_2D_NAME);
		robot.alignWith(ball);
	}
}
