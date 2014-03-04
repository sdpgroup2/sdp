package sdp.navigation;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sdp.communication.CommsInterface;
import sdp.vision.WorldState;

/**
 * This class moves as close to a point as it can, and then stops. Don't try to move the robot before isMoving() returns false
 */
public class PIDMovement extends Thread implements Movement {

	private Pathfinding mPathfinding;
	private boolean mIsMoving;
	private CommsInterface mComms;
	private WorldState mWorldState;
	private double[] constants; //contains the constant to multiply with the error, derivative error, and the integral error
	private double[] errorX; //contains error, derivative error, and integral error
	private double[] errorY; //contains error, derivative error, and integral error
	private ArrayList<Double> integralX;
	private ArrayList<Double> integralY;
	//controlX = c1*error + c2*derivative + c3*integral
	private double controlX;
	//controlY = c1*error + c2*derivative + c3*integral
	private double controlY;
	//controlBearing = the bearing of the vector comprising controlX*i + controlY*j;
	private double controlBearing;
	private double mStartTolerance;
	private double tolerance;
	private int startCount;
	private int count;
	FileWriter fileWriter;
	BufferedWriter bufferedWriter;
	
	/**
	 * Returns the new error vector. Also updates the input Arraylist integral.
	 * @param worldState A reference to the worldstate.
	 * @param theta The error.
	 * @param oldError The error vector from the previous cycle.
	 * @param integral A list of the past 15 error values, which will be updated by this method.
	 * @return [error, derivative of error, integral of error]
	 */
	private double[] updateErrors(WorldState worldState, double theta, double[] oldError, ArrayList<Double> integral) {
		assert (oldError.length==3);
		double decay=0.2;
		double[] newError = new double[3];
		newError[0]=theta;
		newError[1] = (oldError[1]*decay) + (newError[0]-oldError[0])*(1-decay);
		integral.add(newError[0]);
		double sum=0;
		for (int i=0; (i<integral.size()); i++) {
			sum=sum+integral.get(i);
			if (i>15) {integral.remove(0); i--;}
		}
		sum = sum/integral.size();
		newError[2] = sum;
		newError[2] = Math.min(Math.abs(newError[2]),0.1)*(newError[2]/Math.abs(newError[2]));
		return newError;
	}
	
	/**
	 * Beware, timeout is ignored in this implemention of Movement!
	 * 
	 * @param worldState
	 * @param comms
	 * @param timeOutIgnored Not used, only here for compatibility
	 */
	public PIDMovement(WorldState worldState, Pathfinding pathfinding, CommsInterface comms, int timeOutIgnored) {
		mIsMoving=false;
		mComms=comms;
		mWorldState=worldState;
		mPathfinding=pathfinding;
		constants=new double[]{0.24, 0.8, 0};
		errorX=new double[3];
		errorY=new double[3];
		integralX=new ArrayList<Double>();
		integralY=new ArrayList<Double>();
		controlX=0;
		controlY=0;
		controlBearing=0;
		mStartTolerance=14;
		startCount=1;
		
		start();
	}

	private synchronized void setMoving(boolean moving) throws IOException {
		if (mIsMoving && !moving) {
			mComms.stopMoving();
		}
		if (!mIsMoving && moving) {
			errorX[0]=0;
			errorX[1]=0;
			errorX[2]=0;
			errorY[0]=0;
			errorY[1]=0;
			errorY[2]=0;
			integralX.clear();
			integralY.clear();
			tolerance=mStartTolerance;
			count=startCount;
			errorX = updateErrors(mWorldState, (getTargetPoint().getX()-mWorldState.getOurPosition().getX()), errorX, integralX);
			errorX[2]=errorX[0]/4+errorX[1]/2;
			errorY = updateErrors(mWorldState, (getTargetPoint().getY()-mWorldState.getOurPosition().getY()), errorY, integralY);
			errorY[2]=errorY[0]/4+errorY[1]/2;
		}
		mIsMoving = moving;
	}

	public synchronized void stopMoving() throws IOException {
		setMoving(false);
	}
	
	public synchronized boolean isMoving(){
		return mIsMoving;
	}
	
	public synchronized void setTarget(Point targetPoint) throws IOException {
		mPathfinding.setTarget(targetPoint);
		tolerance=mStartTolerance;
		setMoving(true);
	}

	public void run() {
		while (!isInterrupted()) {
			if (mIsMoving) {
				errorX = updateErrors(mWorldState, (getTargetPoint().getX()-mWorldState.getOurPosition().getX()), errorX, integralX);
				errorY = updateErrors(mWorldState, (getTargetPoint().getY()-mWorldState.getOurPosition().getY()), errorY, integralY);
				controlX=constants[0]*errorX[0] + constants[1]*errorX[1] + constants[2]*errorX[2];
				controlY=constants[0]*errorY[0] + constants[1]*errorY[1] + constants[2]*errorY[2];
				controlBearing=Math.atan2(controlY, controlX);
				try {
					mComms.move(controlBearing-mWorldState.getOurOrientation());
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (distanceToTarget()<tolerance) {
					count=count-1;
				} else {
					count=startCount;
				}
				tolerance=tolerance*1.005;
				if (count==0) {
					try {
						setMoving(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}else{
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setAvoidBall(boolean avoidBall) {
		mPathfinding.setAvoidBall(avoidBall);
	}

	public double distanceToTarget() {
		if (getTargetPoint()!=null) {
			return getTargetPoint().distance(mWorldState.getOurPosition());
		} else {
			return 0;
		}
	}
	
	/**
	 * Gets a point for the PID controller to drive towards. There should be no obstacles in the way, 
	 * yet the point should not be so close as to cause noisy behaviour.
	 * @return A point for the PID controller to drive towards.
	 */
	private Point getTargetPoint() {
		ArrayList<Point> path = mPathfinding.getPath();
		int index = Math.min(6, path.size()-1);
		return path.get(index);
	}
}