package sdp.group2.control;

import sdp.group2.light.LightListener;
import sdp.group2.light.MyLightSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

public class Pilot extends DifferentialPilot implements LightListener {
	
	public MyLightSensor leftSensor;
	public static int LightTreshhold = 0;
	public static int WHITE_THRESHOLD = 37;
	public MyLightSensor rightSensor;
	
	public static final int DEFAULT_KICKER_ANGLE = 60;
	public static final int DEFAULT_KICKER_SPEED = Short.MAX_VALUE;

	public void move(int direction, int speed, int distance) {
		LCD.drawString("moving forward!", 10, 10);
		super.travel(direction * distance);
		super.setTravelSpeed(speed);
	} 

	public void rotate(int angle, int speed) {
		LCD.drawString("rotating!", 10, 10);
		super.setRotateSpeed(speed);
		super.rotate(angle);
	}
	
	public void moveForward(double speed) {
		this.setTravelSpeed(speed);
		super.forward();
	}
	
	public void moveBackward(double speed) {
		this.setTravelSpeed(speed);
		super.backward();
	}
	
	public void kick(int angle, int speed) {
		Motor.B.setSpeed(speed);
		Motor.B.rotate(angle);
		Motor.B.rotate(-angle);
	}
	
	public void openKicker(){
		Motor.B.setSpeed(DEFAULT_KICKER_SPEED);
		Motor.B.rotate(DEFAULT_KICKER_ANGLE);
	}
	
	public void closeKicker(){
		Motor.B.setSpeed(DEFAULT_KICKER_SPEED);
		Motor.B.rotate(-DEFAULT_KICKER_ANGLE);
	}
	
	public void rotateKicker(){
		Motor.B.setSpeed(Integer.MAX_VALUE);
		Motor.B.rotate(-360);
	}

	public void stop() {
		LCD.drawString("stopped!", 10, 10);
		super.stop();
	}
	public void steer(double turnRatio) {
		super.steer(turnRatio);
	}

	public Pilot() {
		super(56, 125, Motor.A, Motor.C, true);
		this.leftSensor = new MyLightSensor("L", SensorPort.S4, 100);
		this.leftSensor.addListener(this);
		this.rightSensor = new MyLightSensor("R", SensorPort.S1, 100);
		this.rightSensor.addListener(this);
	}

	public void lightMeasured(int lightVal, MyLightSensor sensor) {
		if (lightVal >= WHITE_THRESHOLD) {
			this.stop();
		}
	}

}
