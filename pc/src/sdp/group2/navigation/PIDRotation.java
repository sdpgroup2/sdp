package sdp.navigation;

import java.io.IOException;
import java.util.ArrayList;

import sdp.common.RotationDirection;
import sdp.communication.CommsInterface;
import sdp.vision.WorldState;

/**
 * This class faces as close to an angle as it can, and then stops. Don't try to move the robot before isRotating() returns false
 */
public class PIDRotation extends Thread implements Rotation {
	
	private boolean mIsRotating;
	private double mAngleToFace;
	private CommsInterface mComms;
	private WorldState mWorldState;
	private double[] constants; //contains the constant to multiply with the error, derivative error, and the integral error
	private double[] error; //contains error, derivative error, and integral error
	//control = c1*error + c2*derivative + c3*integral
	private RotationDirection direction;
	private double mStartTolerance;
	private double tolerance;
	private double control;
	private ArrayList<Double> integral;
	private double rotationThreshold;
	private double mSpeed;
	private int startCount;
	private int count;
	private double scale=3; //use this to scale the signal up and down to account for battery
	
	/**
	 * Returns the new error vector. Also updates the input Arraylist integral.
	 * @param worldState A reference to the worldstate.
	 * @param targetAngle The angle we should turn to.
	 * @param oldError The error vector from the previous cycle.
	 * @param integral A list of the past 15 error values, which will be updated by this method.
	 * @return [error, derivative of error, integral of error]
	 */
	private double[] updateErrors(WorldState worldState, double targetAngle, double[] oldError, ArrayList<Double> integral) {
		assert (oldError.length==3);
		double decay=0.2;
		double theta = worldState.getOurOrientation();
		double[] newError = new double[3];
		newError[0] = targetAngle-theta;
		if (newError[0]>Math.PI) {
			newError[0]=newError[0]-(2*Math.PI);
		}
		if (newError[0]<-Math.PI) {
			newError[0]=newError[0]+(2*Math.PI);
		}
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
	 * Beware, timeout is ignored in this implemention of Rotation!
	 * 
	 * @param worldState
	 * @param comms
	 * @param timeOutIgnored Not used, only here for compatibility
	 */
	public PIDRotation(WorldState worldState, CommsInterface comms, int timeOutIgnored) {
		mIsRotating=false;
		mAngleToFace=0;
		mComms=comms;
		mWorldState=worldState;
		constants=new double[]{0.24, 0.8, 0.5};
		error=new double[3];
		direction=RotationDirection.CLOCKWISE;
		tolerance=0.1;
		control=0;
		integral=new ArrayList<Double>();
		rotationThreshold=0.15;
		startCount=4;
		count=startCount;
		
		start();
	}

	private synchronized void setRotating(boolean rotating) throws IOException {
		if (mIsRotating && !rotating) {
			mComms.stopRotating();
		}
		if (!mIsRotating && rotating) {
			error[0]=0;
			error[1]=0;
			error[2]=0;
			integral.clear();
			tolerance=mStartTolerance;
			count=startCount;
			error = updateErrors(mWorldState, mAngleToFace, error, integral);
			error[2]=error[0]/4+error[1]/2;
		}
		mIsRotating = rotating;
	}

	public synchronized void stopRotating() throws IOException {
		setRotating(false);
	}
	
	public synchronized boolean isRotating(){
		return mIsRotating;
	}
	
	/**
	 * Sets the angle to rotate to
	 * 
	 * @param angle The angle in radians, between 0 and 2PI
	 * @param speed The speed of the rotation. Can be any value above 0, to scale the speed in response to battery level.
	 * @param tolerance How close it should try to get the angle
	 */
	public synchronized void setTargetAngle(double angle, double speed, double startTolerance) throws IOException {
		if ( angle < 0 || angle > Math.PI*2 ){
			//throw new IllegalArgumentException("Angle should be between 0 and 2PI, but equals "+angle);
		}
		if (Math.abs(angle-mAngleToFace)>startTolerance) {
			mStartTolerance = startTolerance*0.5; // set this to the same scale as the other rotation method's parameters
			mAngleToFace = angle;
			mSpeed = speed*scale; // set this to the same scale as the other rotation method's parameters
		}
		setRotating(true);
	}

	public void run() {
		while (!isInterrupted()) {
			if (mIsRotating) {
				error = updateErrors(mWorldState, mAngleToFace, error, integral);
				control=constants[0]*error[0] + constants[1]*error[1] + constants[2]*error[2];
				if (count==startCount) {
					control=control+rotationThreshold*(control/Math.abs(control)); //correct to ensure that the signal is proportional to movement produced
				}
				control=control*mSpeed;
				if (control>0) {
					control=Math.min(control,1);
					direction=RotationDirection.COUNTERCLOCKWISE;
				} else {
					control=Math.max(control,-1);
					direction=RotationDirection.CLOCKWISE;
				}
				try {
					mComms.rotate(direction, control);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (Math.abs(error[0])<tolerance) {
					count=count-1;
				} else {
					count=startCount;
				}
				tolerance=tolerance*1.02;
				if (count==0) {
					try {
						setRotating(false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		try {
			mComms.stopRotating();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public synchronized double angleToTarget(){
            double angleDiff = mWorldState.getOurOrientation() - mAngleToFace;
            if ( angleDiff > Math.PI)
                    angleDiff -= 2*Math.PI;
            if ( angleDiff < -Math.PI)
                    angleDiff += 2*Math.PI;
            return angleDiff * -1;
    }
}
