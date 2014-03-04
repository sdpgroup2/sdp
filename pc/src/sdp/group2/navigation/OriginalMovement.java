package sdp.navigation;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import sdp.communication.CommsInterface;
import sdp.vision.WorldState;

/**
 * Moves to a point and when it thinks it has reached that point it stops
 * 
 * If it has reached the point can be detected by isMoving();
 *
 */
public class OriginalMovement extends Thread implements Movement {

	private boolean mMoving;

	private Pathfinding mPathfinding;
	private CommsInterface mComms;

	private int mTimeout;
	private WorldState mWorldState;

	public OriginalMovement(WorldState worldState, Pathfinding pathfinding, CommsInterface comms, int timeout) {
		mPathfinding = pathfinding;
		mComms = comms;
		mTimeout = timeout;
		mWorldState = worldState;
		
		start();
	}

	/**
	 * Allows starting or stopping moving towards the target 
	 * @param moving
	 * @throws IOException 
	 */
	private synchronized void setMoving(boolean moving) throws IOException {
		if ( mMoving && !moving ){
			mComms.stopMoving();
		}
		mMoving = moving;
	}
	
	public synchronized void setAvoidBall(boolean avoidBall){
		mPathfinding.setAvoidBall(avoidBall);
	}
	
	/**
	 * Returns if the robot is moving. If the robot was moving and has now
	 * reached the target, this will return false
	 */
	public synchronized boolean isMoving(){
		return mMoving;
	}

	/**
	 * Set the target to go towards
	 * @param target
	 */
	public synchronized void setTarget(Point target) throws IOException {
		mPathfinding.setTarget(target);
		setMoving(true);
	}

	/**
	 * Code that actually goes towards the target.
	 * 
	 * As this is a thread start this through the start function.
	 */
	public void run() {
		while ( !isInterrupted() ) {
			ArrayList<Point> currentPath = mPathfinding.getPath();
						
			if (!mMoving || currentPath == null ) {
				try {
					Thread.sleep(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			
			//take a point a decent length away to go to
			//TODO this is really hacky
			int targetPointIdx = 1;
			for ( int i = 1; i < currentPath.size(); ++i ) {
				Point ithPoint = currentPath.get(i);
				
				if ( i > currentPath.size() - 2 ){
					if ( distanceFrom(ithPoint.x, ithPoint.y) < 35 ){
						targetPointIdx = i + 1;
					}
				}else{
					if ( distanceFrom(ithPoint.x, ithPoint.y) < 50 ){
						targetPointIdx = i + 1;
					}
				}
			}
			
			if ( targetPointIdx >= currentPath.size() ) {
				try {
					setMoving(false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			Point target = currentPath.get(targetPointIdx);
			
			try {
				moveToPoint(target.x, target.y);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			try {
				Thread.sleep(mTimeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gives the distance between from our robot to the point it receives.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public double distanceFrom(int x, int y) {
		final Point2D ourPoint = new Point2D.Double(mWorldState.getOurX(), mWorldState.getOurY());
		final Point2D theirPoint = new Point2D.Double(x, y);
		return ourPoint.distance(theirPoint);
	}

	/**
	 * Returns the angle by which our direction must change in order to have the
	 * point straight ahead of us. Positive angle means counterclockwise
	 * rotation and negative angle means clockwise rotation.
	 * 
	 * @param x
	 * @param y
	 * @returnBasicStrategy
	 */
	public double angleTo(int x, int y) {
		final double o = mWorldState.getOurOrientation();
		final int ourX = mWorldState.getOurX();
		final int ourY = mWorldState.getOurY();
		final Line2D line1 = new Line2D.Double(ourX, ourY, ourX + Math.cos(o), ourY + Math.sin(o));
		final Line2D line2 = new Line2D.Double(ourX, ourY, x, y);

		final double angle1 = Math.atan2(line1.getY1() - line1.getY2(), line1.getX1() - line1.getX2());
		final double angle2 = Math.atan2(line2.getY1() - line2.getY2(), line2.getX1() - line2.getX2());
		double angle = -1 * (angle1 - angle2);

		if (angle > Math.PI) {
			angle -= 2 * Math.PI;
		}
		if (angle < -Math.PI) {
			angle += 2 * Math.PI;
		}
		return angle;
	}

	public void moveToPoint(int x, int y) throws IOException {
		if (mWorldState.getOurX() == x && mWorldState.getOurY() == y) {
			System.out.println("Already at point (" + x + ", " + y + ")");
			return;
		}
		final float angle = (float) angleTo(x, y);
		mComms.move(angle);
	}

	public void stopMoving() throws IOException {
		setMoving(false);
	}

	public double distanceToTarget() {
		return mPathfinding.getPath().get(mPathfinding.getPath().size()-1).distance(mWorldState.getOurPosition());
	}
}
