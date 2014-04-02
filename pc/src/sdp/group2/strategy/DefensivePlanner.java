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
		Debug.log("Ball: %s", pitch.getBall().getPosition());
//		frames += 1;
		int ballZoneId = pitch.getBallZone();
		int defenderZoneId = pitch.getOurDefendZone();
		
		if (ballZoneId != defenderZoneId) {
			defend();
		} else {
			pass();
		}
	}
	
	public void pass() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();
//		Point pitchCenter;
//		if (MasterController.pitchPlayed == PitchType.MAIN) {
//			pitchCenter = Constants.PITCH0_CENTER;
//		} else {
//			pitchCenter = Constants.PITCH1_CENTER;
//		}
//		
//		
//		switch (robot.getState()) {
//		case CAN_PASS:
//			robot.kick();
//			robot.setState(RobotState.FIND_BALL);
//			return;
//		case HAS_BALL:
//			robot.alignWith(pitchCenter);
//			if (robot.shouldPassAlign()) {
//				robot.passAllign();
//			} else if (robot.distanceTo(new Point(robot.getPosition().x, ))){
//				robot.setState(RobotState.CAN_PASS);
//			}
//			break;
//			
//		case CAN_GRAB:
//			robot.closeKicker();
//			System.out.println("Closed the kicker");
//			break;
//			
//		case FIND_BALL:
//			break;
//		
//		default:
//			break;
//		}
//		
//		if (robot.getState() == RobotState.FIND_BALL) {
//			
//		}

		System.out.println("Kicker: " + robot.isKickerOpen());
		System.out.println(robot.distanceTo(ball));
		if (!robot.isKickerOpen() && !robot.hasBall(ball)) {
//			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			robot.openKicker();
			System.out.println("Opened the kicker!");
			return;
			
		} else if (robot.hasBall(ball) && !robot.isKickerOpen()) {
//			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			// Angle ranges from -180 to 180 degrees.
			if (robot.inCenter(getPitch())) {
				System.out.println("In the center");
				if (robot.shouldPassAlign()) {
					System.out.println("Trying to align for passing");
					robot.passAlign();	
					return;
				} else {
					System.out.println("Kicking");
					robot.kick();
					try {
						Thread.sleep(150);
					} catch (Exception e) {
						
					}
					return;
				}
			} else {
				System.out.println("Trying to go to center");
				robot.alignWith(pitch.getCenter(), 100);
				return;
			}
		}
//		System.out.println("Don't have the ball!");
		
		if (!ball.isStable() && !robot.canGrab(ball)) {
			System.out.println("ball not stable");
			return;
		}
//		System.out.println("Ball is stable!");
		
		if (robot.canGrab(ball) && robot.isKickerOpen()) {
//			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			robot.closeKicker();
			System.out.println("Closed the kicker!");
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
//			CommandQueue.clear(Constants.ROBOT_2D_NAME);
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
		
		if (robot.shouldDefenceAlign()) {
			robot.defenceAlign();
			return;
		}
		
		CommandQueue.clear(Constants.ROBOT_2D_NAME);
		robot.alignWith(ball, 400);
	}
}
