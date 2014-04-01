package sdp.group2.strategy;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.geometry.Point;
import sdp.group2.util.Constants;
import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;
import sdp.group2.world.Robot;

public class DefensivePlanner extends Planner {
	
	private static final String robotName = Constants.ROBOT_2D_NAME;
	
	// The number of frames in between commands
	private int STUTTER_FRAMES = 1;
	
	// The min/max Y that the robot is allowed to have. (The goal posts)
	private static final double minY = 365;
	private static final double maxY = 1026;
	
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
			stutter();
		} else {
			pass();
		}
	}
	
	public void pass() {
		Robot robot = pitch.getOurDefender();
		Ball ball = pitch.getBall();
//		System.out.println("Robot Position: " + robot.getPosition());
//		System.out.println("Ball Position: " + ball.getPosition());
		System.out.println("Angle to ball: " + robot.angleTo(ball));
		System.out.println("Distance to ball: " + robot.distanceTo(ball));
		if (robot.haveBall(ball)) {
			robot.kick();
		}
//		Robot robot = pitch.getOurDefender();
//		
//		// Angle ranges from -180 to 180 degrees.		
//		double angleToBall = robot.angleTo(pitch.getBall());
//		System.out.printf("Angle to the ball: %f\n", angleToBall);
//		
//		boolean wrongAngle = Math.abs(angleToBall) > 5;
//		
//		// Do this every 20 frames, starting from frame 0
//		if (frames % STUTTER_FRAMES == 0) {
//			if (wrongAngle) {
//				Debug.log("Too big an angle difference, rotating.");
//				// angleSign is negative for an anticlockwise rotation and positive for a clockwise.
////				angleSign = Math.signum(angleToBall);
//				CommandQueue.add(Commands.rotate((int) angleToBall, 20), robotName);
//			} else {
//				Debug.log("Angle is fine.");
//			}
//		}
//		
//		// Do this every 20 frames, starting from frame 10
//		else if (frames % STUTTER_FRAMES == STUTTER_FRAMES / 2) {
//			
//			double distanceToGo = robot.vectorTo(pitch.getBall()).length();
//			
//			// Multiply by 0.9 so that we don't hit the wall and stuff
//			distanceToGo = 0.9 * distanceToGo;
//			
//			// If the distance is too great, and the robot is roughly vertically aligned:
//			if (distanceToGo > 30 && !wrongAngle) {
//				CommandQueue.clear(robotName);
//				CommandQueue.add(Commands.move(-1, 100, (int) distanceToGo), robotName);
//			} else {
////				CommandQueue.clear(robotName);
////				CommandQueue.add(Commands.kick(Constants.DEF_KICK_ANGLE, Constants.DEF_KICK_POWER), robotName);
//			}
//		}
		
	}
	
	/**
	 * Every STUTTER_FRAMES frames, either adjust the angle of the defending robot to be
	 * more vertical (in line with goal) OR move the defending robot forwards or backwards
	 * trying to match the ball's position.
	 */
	public void stutter() {
		Robot robot = pitch.getOurDefender();
		
		// Angle ranges from -180 to 180 degrees.
		double angle = robot.angleToX();
		System.out.println(angle);
		double angleSign = Math.signum(angle);
		double unsignedAngle = Math.abs(angle);
		
		// The angle is wrong if it is more than 10 degrees away from 90.
		boolean wrongAngle = !(80 < unsignedAngle && unsignedAngle < 100);
		
		if (wrongAngle) {
			double toRotate = angleSign * (90 - unsignedAngle);
			System.out.printf("Rotate by: %f.2\n", toRotate);
			System.out.println(CommandQueue.commandQueue2D.size());
	    	robot.rotate(toRotate);
		} else {
			CommandQueue.clear(Constants.ROBOT_2D_NAME);
			double ry = robot.getPosition().y;
			double by = pitch.getBall().getPosition().y;
			
			// angleSign is 1 if the robot is facing upwards and -1 if downwards.
			// ballDir is 1 if the ball is above, -1 if the ball is below.
			// dir multiplies these two to find the direction in which to move.
			int ballDir = (int) Math.signum(ry - by);
			int dir = (int) (ballDir * angleSign);
			
			int dist = (int) Math.abs(ry - by);
			
			// If the ball is outwith the range of the goal post, we only want to move as far as the goal post
			if (by <= minY) {
				dist = (int) Math.abs(minY - ry);
			} else if (by >= maxY) {
				dist = (int) Math.abs(maxY - ry);
			// Printout PID adjustment
			double u = pid.getAdjustment(unsignedAngle);
			System.out.println("robot angle adjustment = " + u);
			// ---
			
			if (wrongAngle) {
				double toRotate = angleSign * (90 - unsignedAngle);
				System.out.printf("Rotate by: %f.2\n", toRotate);
				System.out.println(CommandQueue.commandQueue2D.size());
				robot.rotate(toRotate);
			} else {
				CommandQueue.clear(Constants.ROBOT_2D_NAME);
			}
			
			// Multiply by 0.9 so that we don't hit the wall and stuff
			dist = (int) (0.9 * dist);
			
			// If the distance is too great, and the robot is roughly vertically aligned:
//			robot.goTo(new Point(robot.getPosition().x, ball.getPosition().y), false);
			robot.forward(-dir, dist);
		}
		
//		// Do this every 20 frames, starting from frame 0
//		if (frames % STUTTER_FRAMES == 0) {
//			Debug.logf("Defender angle is: %f", angle);
//			if (wrongAngle) {
//				Debug.log("Too big an angle difference, rotating.");
//				// angleSign is negative for an anticlockwise rotation and positive for a clockwise.
//				double angleSign = Math.signum(angle);
//				CommandQueue.add(Commands.rotate((int) (angleSign * (90 - unsignedAngle)), 100), robotName);
//			} else {
//				Debug.log("Angle is fine.");
//			}
//		} else if (frames % STUTTER_FRAMES == STUTTER_FRAMES / 2) {
//			// Do this every 20 frames, starting from frame 10
//			double ry = robot.getPosition().y;
//			double by = pitch.getBall().getPosition().y;
//			
//			// angleSign is 1 if the robot is facing upwards and -1 if downwards.
//			// ballDir is 1 if the ball is above, -1 if the ball is below.
//			// dir multiplies these two to find the direction in which to move.
//			double angleSign = Math.signum(angle);
//			int ballDir = (int) Math.signum(ry - by);
//			int dir = (int) (ballDir * angleSign);
//			
//			int dist = (int) Math.abs(ry - by);
//			
//			// If the ball is outwith the range of the goal post, we only want to move as far as the goal post
//			if (by <= minY) {
//				dist = (int) Math.abs(minY - ry);
//			} else if (by >= maxY) {
//				dist = (int) Math.abs(maxY - ry);
//			}
//			
//			// Multiply by 0.9 so that we don't hit the wall and stuff
//			dist = (int) (0.9 * dist);
//			
//			// If the distance is too great, and the robot is roughly vertically aligned:
//			if (dist > 30 && !wrongAngle) {
//				CommandQueue.clear(robotName);
//				CommandQueue.add(Commands.move(dir, 1500, dist), robotName);
//			}
//		}
	}
	
}
