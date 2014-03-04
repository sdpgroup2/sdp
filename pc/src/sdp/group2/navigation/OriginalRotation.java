package sdp.navigation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sdp.communication.CommsInterface;
import sdp.vision.WorldState;

/**
 * This class allows the robot to face a point and it will always try to maintain that angle
 * 
 * Unlike movement it doesn't stop when it faces and angle, but rather it keeps trying to correct
 * the angle it is facing 
 * 
 */
public class OriginalRotation extends Thread {
	
	private boolean mIsRotating = false;
	private double mAngleToFace = 0;
	private CommsInterface mComms;
	private int mTimeout;
	private WorldState mWorldState;
	private double mTolerance;
	private double mSpeed;
	private double mCurrentSpeed = 0;

	
	private boolean mHasStopped = false;
	private List<Double> mErrors = new ArrayList<Double>();
	/**
	 * 
	 * @param worldState
	 * @param comms
	 * @param timeout How long to sleep after a single loop checking the robot direction etc
	 */
	public OriginalRotation(WorldState worldState, CommsInterface comms, int timeout) {
		mComms = comms;
		mWorldState = worldState;
		mTimeout = timeout;
		
		start();
	}

	public synchronized void setRotating(boolean rotating) throws IOException {
		if (mIsRotating && !rotating) {
			mComms.stopRotating();
		}
		mIsRotating = rotating;
	}
	
	public synchronized boolean isRotating(){
		return mIsRotating;
	}
	
	private double mean(Collection<Double> list){
		if ( list.size() == 0 ){
			return 0;
		}
		
		double sum = 0;
		for ( Double d : list){
			sum += d;
		}
		return sum/list.size();
	}
	
	/**
	 * Sets the angle to rotate to
	 * 
	 * @param angle The angle in radians, between 0 and 2PI
	 * @param speed The speed of the rotation
	 * @param tolerance How close it should try to get the angle
	 */

	public synchronized void setTargetAngle(double angle, double speed, double tolerance) throws IOException {
		if ( angle < 0 || angle > Math.PI*2 ){
			throw new IllegalArgumentException("Angle should be between 0 and 2PI");
		}
		mAngleToFace = angle;
		mTolerance = tolerance;
		mSpeed = mCurrentSpeed = speed;
		mHasStopped = false;
		setRotating(true);
	}
	
	public synchronized double angleToTarget(){
		double angleDiff = mWorldState.getOurOrientation() - mAngleToFace;
		if ( angleDiff > Math.PI)
			angleDiff -= 2*Math.PI;
		if ( angleDiff < -Math.PI)
			angleDiff += 2*Math.PI;
		return angleDiff * -1;
	}

	public void run() {
		int lastSignSent = 0;
		double lastSpeedSent = 0;
		double lastAngleToTarget = 0;
		while (!isInterrupted()) {
			double angleToTarget =  angleToTarget();
						
			if (!isRotating() || Math.abs(angleToTarget) < mTolerance) {

				try {
					if ( Math.abs(angleToTarget) < mTolerance ){
						try {
							if ( lastSignSent != 0 ){
								lastSignSent = 0;
								mComms.rotate(lastSignSent*-1, mSpeed);
								Thread.sleep(50);
								mComms.stopRotating();
								mHasStopped = true;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					Thread.sleep(mTimeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			
			int sign = angleToTarget < 0 ? -1 : 1;
			try {
				mCurrentSpeed = mSpeed*getSpeedMult(Math.abs(angleToTarget));
				
				if (lastSignSent != sign ){
					//if we have changed direction, we always try and rotate at full 
					//speed initially
					mComms.rotate(angleToTarget, mSpeed);
					lastSignSent = sign;
				}else if ( Double.compare(mCurrentSpeed, lastSpeedSent) != 0) {
					mComms.rotate(angleToTarget, mCurrentSpeed);
					lastSpeedSent = mCurrentSpeed;
					System.out.println("current speed is: " + mCurrentSpeed);
					lastSignSent = sign;

				}
			}catch(IOException e){
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(mTimeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	

	private double getSpeedMult(double angleToTarget){
		/*
		final double startSlowingDown = (Math.PI)/mSpeed;
		if ( angleToTarget < startSlowingDown ){
			return angleToTarget/startSlowingDown;
		}
		return 1;
		*/
		//if ( angleToTarget < Math.PI/2)
		//	return Math.min(1, 2*(Math.cos(2*angleToTarget + Math.PI) + 1));
		//return 1;
		return 1;//Math.max(0, Math.min(1, 3*(Math.log (angleToTarget/2 + 1))));
	}

}