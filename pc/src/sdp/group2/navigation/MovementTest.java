package sdp.navigation;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import sdp.common.RotationDirection;
import sdp.communication.CommsClient;
import sdp.gui.MainWindow;
import sdp.strategy.KickFrom;
import sdp.vision.Drawable;
import sdp.vision.DrawableLine;
import sdp.vision.NoAngleException;
import sdp.vision.Position;
import sdp.vision.RunVision;
import sdp.vision.Vision;
import sdp.vision.WorldState;

public class MovementTest {
	private WorldState mWorldState;
	private Vision mVision;
	CommsClient c;

	double[] error = new double[3]; //contains error, derivative error, and integral error
	//control = c1*error + c2*derivative + c3*integral
	double[] constants = new double[]{0.24, 0.8, 0.5}; //contains the constant to multiply with the error, derivative error, and the integral error
	double angleToTurnTo;
	double angleToTurn;
	double ourOrient;
	RotationDirection direction;
	double tolerance;
	double control;
	ArrayList<Double> integral;
	double rotationThreshold=0.1;
	double scale = 0.6; //use this to scale the signal up and down to account for battery
	
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
	
	public void pidRotation() throws InterruptedException, IOException {
		Point targetPoint;
		Collection<Drawable> orientationGraphics = new ArrayList<Drawable>();
		for (int i=1; (i<10); i++) {
			try {
				tolerance=0.1;
				integral = new ArrayList<Double>();
				Thread.sleep(3000);
				ourOrient = mWorldState.getOurOrientation();
				System.out.println("Our 4Starting Orientation:"+String.format("%.3f", ourOrient));
				
				targetPoint = new Point((int) (Math.random()*mWorldState.getPitchWidth()),
										(int) (Math.random()*mWorldState.getPitchHeight()));
				
				orientationGraphics.clear();
				orientationGraphics.add(new DrawableLine(Color.BLACK, mWorldState.getOurPosition(), targetPoint));
				MainWindow.addOrUpdateDrawable("Target Orientation", orientationGraphics);
				
				angleToTurnTo = Position.angleTo(mWorldState.getOurPosition(), targetPoint);

				System.out.println("angleToTurnTo="+String.format("%.3f", angleToTurnTo));
				error[0]=0;
				error[1]=0;
				error[2]=0;
				error = updateErrors(mWorldState, angleToTurnTo, error, integral);
				error[2]=error[0]/4+error[1]/2;
				
				int count=5;
				int lastcount=count;
				while (count>0) {
				    angleToTurn = angleToTurnTo - mWorldState.getOurOrientation();
					error = updateErrors(mWorldState, angleToTurnTo, error, integral);
					control=constants[0]*error[0] + constants[1]*error[1] + constants[2]*error[2];
					if (count==lastcount) {
						control=control+rotationThreshold*(control/Math.abs(control)); //correct to ensure that the signal is proportional to movement produced
					}
					control=control*scale;
					if (control>0) {
						control=Math.min(control,1);
						System.out.println("Clockwise");
						direction=RotationDirection.COUNTERCLOCKWISE;
					} else {
						control=Math.max(control,-1);
						System.out.println("Counterclockwise");
						direction=RotationDirection.CLOCKWISE;
					}
				    System.out.println("mWorldState.getOurOrientation()="+String.format("%.3f", mWorldState.getOurOrientation()));
				    System.out.println("angleToTurnTo="+String.format("%.3f", angleToTurnTo));
				    System.out.println("control="+String.format("%.3f", control));
					System.out.println("Error = ["+String.format("%.3f", error[0])+", "+String.format("%.3f", error[1])+", "+String.format("%.3f", error[2])+"]");
					System.out.println("k*e = ["+String.format("%.3f", error[0]*constants[0])+", "+String.format("%.3f", error[1]*constants[1])+", "+String.format("%.3f", error[2]*constants[2])+"]");
					//FileWriter fileWriter = new FileWriter("constants/rotationData.txt", true);
					//BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					//bufferedWriter.write(String.format("%.3f", control)+"\t"+String.format("%.3f", error[0]*constants[0])+"\t"+String.format("%.3f", error[1]*constants[1])+"\t"+String.format("%.3f", error[2]*constants[2])+"\n");
					//bufferedWriter.close();
					//fileWriter.close();
					c.rotate(direction, control);
					Thread.sleep(100);
					if (Math.abs(error[0])<tolerance) {
						count=count-1;
					} else {
						count=5;
					}
					tolerance=tolerance*1.02;
				}
				//Done turning, stop now
				c.stopRotating();
				Thread.sleep(1000);
				
				//Stopped
			} catch (NoAngleException e) {
				angleToTurnTo = 0;
				e.printStackTrace();
				System.out.println("No such angle exists");
			}
		}
		System.out.println("Finished Rotating");
		System.out.println("tolerance="+tolerance);
	}

	public MovementTest() throws InterruptedException, IOException {
		c = new CommsClient();
		c.connect();
		c.setMaximumSpeed(255);
		mWorldState = new WorldState();
		mVision = RunVision.setupVision(mWorldState);
//		movement();
		rotation();
//		pidRotation();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException{
		new MovementTest();
	}
	
	public void movement() throws InterruptedException, IOException{

		Thread.sleep(3000);

		Pathfinding p = new AStarPathfinding(mWorldState);
		p.setAvoidBall(false);
		Movement m = new PIDMovement(mWorldState, p, c, 40);
		m.setAvoidBall(false);

		do {
			m.setTarget(KickFrom.whereToKickFrom(mWorldState));
			Thread.sleep(10);
		} while (m.isMoving());
	}
	
	public void rotation() throws InterruptedException, IOException{

		Thread.sleep(3000);
		double speed = 0.20;

		PIDRotation r = new PIDRotation(mWorldState, c, 0);

		Point targetPoint;
		Collection<Drawable> orientationGraphics = new ArrayList<Drawable>();
		targetPoint = new Point((int) (Math.random()*mWorldState.getPitchWidth()),
								(int) (Math.random()*mWorldState.getPitchHeight()));
		targetPoint= mWorldState.getBallPoint();
		orientationGraphics.clear();
		orientationGraphics.add(new DrawableLine(Color.BLACK, mWorldState.getOurPosition(), targetPoint));
		MainWindow.addOrUpdateDrawable("Target Orientation", orientationGraphics);
		
		try {
			angleToTurnTo = Position.angleTo(mWorldState.getOurPosition(), targetPoint);
		} catch (NoAngleException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			r.setTargetAngle(Position.angleTo(mWorldState.getOurPosition(), mWorldState.getBallPoint()), speed, 0.2);
		} catch (NoAngleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (r.isRotating()) {
			Thread.sleep(50);
		}
		
		//Thread.sleep(4000);
		//r.setRotating(false);
		//c.stopRotating();
		System.out.println(mWorldState.getOurOrientation());
	}
	
}
