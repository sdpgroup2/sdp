package sdp.navigation;

import java.awt.Point;
import java.io.IOException;

public interface Movement extends Runnable {
	
	public void stopMoving() throws IOException;
	
	/**
	 * Returns if the robot is moving. If the robot was moving and has now
	 * reached the target, this will return false
	 */
	public boolean isMoving();
	public void setAvoidBall(boolean avoidBall);
	
	/**
	 * Set the target to go towards
	 * @param target
	 */
	public void setTarget(Point targetPoint) throws IOException;    

	/**
	 * Gives the distance between from our robot to the point it receives.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
    public double distanceToTarget();

	/**
	 * Code that actually goes towards the target.
	 * 
	 * As this is a thread start this through the start function.
	 */
	public void run();
}
