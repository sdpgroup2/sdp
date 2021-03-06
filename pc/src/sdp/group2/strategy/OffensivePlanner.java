package sdp.group2.strategy;

import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;
import sdp.group2.world.Robot;

public class OffensivePlanner extends Planner {
	
	
	public OffensivePlanner(Pitch pitch) {
        super(pitch);
    }
	
	public void act() {
		int ballZoneId = pitch.getBallZone();
		int attackerZoneId = pitch.getOurAttackZone();
		System.out.println("\n\n------------------------------------------");
		System.out.println("---------------ATTACKER-------------------");
		System.out.println("Distance to ball:" + pitch.getOurAttacker().distanceTo(pitch.getBall()));
		System.out.println("Angle to ball:" + pitch.getOurAttacker().angleTo(pitch.getBall()));
//		System.out.println("Robot position: " + pitch.getOurDefender().getPosition());
//		System.out.println("Ball position: " + pitch.getBall().getPosition());
		if (ballZoneId != attackerZoneId || pitch.getBall().enteringZone(attackerZoneId)) {
			defend();
		} else {
			pass();
		}
		System.out.println("------------------------------------------");
	}
	
	public void pass() {
		Robot robot = pitch.getOurAttacker();
		Ball ball = pitch.getBall();

//		System.out.println("Kicker: " + robot.isKickerOpen());
//		System.out.println("Distance to ball: " + robot.distanceTo(ball));
//		System.out.println("Angle to ball: " + robot.angleTo(ball));
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
					double angleToDefender = robot.angleTo(pitch.getFoeDefender().getPosition());
					System.out.println("Angle to defender: " + angleToDefender);
					if (Math.abs(angleToDefender) < 10) {
						robot.kick360();
					} else {
						robot.kick();
					}
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
//		System.out.println("Angle to ball:" + angle);
//		System.out.println("Distance to ball:" + robot.distanceTo(ball));
//		System.out.println("Ball position:" + ball.getPosition());
//		System.out.println("Robot position:" + robot.getPosition());
		
		if (wrongAngle) {
	    	robot.rotate(- 0.7 * angle);
		} else {
			int dist = (int) robot.distanceTo(ball);
			// Multiply by 0.9 so that we don't hit the wall and stuff
			dist = (int) (0.8 * dist);
			
			// If the distance is too great, and the robot is roughly vertically aligned:
			robot.forward(1, dist, 100);
		}
		
	}
	
	/**
	 * Do this when we don't have the ball
	**/
	public void defend() {
		Robot robot = pitch.getOurAttacker();
		Ball ball = pitch.getBall();
		
		if (robot.shouldDefenceAlign()) {
			robot.defenceAlign();
			return;
		}
		
		robot.alignWith(ball, 1000, 25);
	}
}
