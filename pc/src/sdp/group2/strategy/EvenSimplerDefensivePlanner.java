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
	
	// The number of frames in between commands
	private int STUTTER_FRAMES = 20;
	
	// The min/max Y that the robot is allowed to have. (The goal posts)
	private static final double minY = 285;
	private static final double maxY = 853;
	
	// The number of frames this has been running.
	private int frames = 0;
	
	
	public EvenSimplerDefensivePlanner(IPitch pitch) {
        super(pitch);
    }
	
	public void act() {
		frames += 1;
		stutter();
	}
	
	/**
	 * Every STUTTER_FRAMES frames, either adjust the angle of the defending robot to be
	 * more vertical (in line with goal) OR move the defending robot forwards or backwards
	 * trying to match the ball's position.
	 */
	public void stutter() {
		Robot robot = pitch.getOurDefender();
		
		// Angle ranges from -180 to 180 degrees.
		double angle = robot.getFacingVector().signedAngleDegrees();
		double unsignedAngle = Math.abs(angle);
		
		// The angle is wrong if it is more than 10 degrees away from 90.
		boolean wrongAngle = !(80 < unsignedAngle && unsignedAngle < 100);
		
		// Do this every 20 frames, starting from frame 0
		if (frames%STUTTER_FRAMES == 0) {
			Debug.logf("  Defender angle is: %f", angle);
			if (wrongAngle) {
				Debug.log("Too big an angle difference, rotating.");
				// angleSign is negative for an anticlockwise rotation and positive for a clockwise.
				double angleSign = Math.signum(angle);
				CommandQueue.add(Commands.rotate((int)(angleSign*(90-unsignedAngle)), Short.MAX_VALUE), robotName);
			} else {
				Debug.log("Angle is fine.");
			}
		}
		
		// Do this every 20 frames, starting from frame 10
		else if (frames%STUTTER_FRAMES == STUTTER_FRAMES/2) {
			double ry = robot.getPosition().y;
			double by = pitch.getBall().getPosition().y;
			
			// angleSign is 1 if the robot is facing upwards and -1 if downwards.
			// ballDir is 1 if the ball is above, -1 if the ball is below.
			// dir multiplies these two to find the direction in which to move.
			double angleSign = Math.signum(angle);
			int ballDir = (int) Math.signum(ry-by);
			int dir = (int) (ballDir * angleSign);
			
			int dist = (int) Math.abs(ry-by);
			
			// If the ball is outwith the range of the goal post, we only want to move as far as the goal post
			if (by <= minY) {
				dist = (int) Math.abs(minY - ry);
			} else if (by >= maxY) {
				dist = (int) Math.abs(maxY - ry);
			}
			
			// If the distance is too great, and the robot is roughly vertically aligned:
			if (dist > 30 && !wrongAngle) {
				CommandQueue.clear(robotName);
				CommandQueue.add(Commands.move(dir, 5000, dist), robotName);
			}
		}
	}
	
}
