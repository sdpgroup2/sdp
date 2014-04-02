package sdp.group2.control;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.RCXMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import sdp.group2.light.LightListener;
import sdp.group2.light.MyLightSensor;

public class Pilot extends DifferentialPilot implements LightListener {
	
	public MyLightSensor leftSensor;
	public static int LightTreshhold = 0;
	public static int WHITE_THRESHOLD = 37;
	public MyLightSensor rightSensor;
	public RCXMotor kicker;
	
	public static final int DEFAULT_KICKER_ANGLE = 3;
	public static final int DEFAULT_KICKER_SPEED = Short.MAX_VALUE;

	public void move(int direction, int speed, int distance) {
		super.setTravelSpeed(speed);
		LCD.drawString("moving forward!", 10, 10);
		super.travel(direction * distance);
		
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
	
	public void kick(int angle, int speed) {
		kicker.setPower(100);
		kicker.forward();
		Delay.msDelay(150);
		try {
			Thread.sleep(150);
		} catch (Exception e) {
			
		}
		kicker.stop();
	}
	
	public void openKicker() {
		System.out.println("About to open");
		kicker.setPower(60);
		kicker.backward();
		Delay.msDelay(150);
		try {
			Thread.sleep(150);
		} catch (Exception e) {
			
		}
		kicker.stop();
		System.out.println("Opened");
	}
	
	public void closeKicker() {
		kicker.setPower(80);
		kicker.forward();
		Delay.msDelay(150);
		try {
			Thread.sleep(150);
		} catch (Exception e) {
			
		}
		kicker.stop();
	}
	
	public void rotateKicker() {
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
		this.kicker = new RCXMotor(MotorPort.B);
	}

	public void lightMeasured(int lightVal, MyLightSensor sensor) {
		if (lightVal >= WHITE_THRESHOLD) {
			this.stop();
		}
	}

}
