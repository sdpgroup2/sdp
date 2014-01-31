package group2.sdp.robot;

import group2.sdp.robot.light.LightListener;
import group2.sdp.robot.light.MyLightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

public class Pilot extends DifferentialPilot implements LightListener {
	
	public MyLightSensor leftSensor;
	public static int LightTreshhold = 0;
	public static int WHITE_THRESHOLD = 37;
	public MyLightSensor rightSensor;

	public void move(double direction) {
	}

	public void moveForward(double speed) {
		this.setTravelSpeed(speed);
		this.forward();
	}
	
	public static void kick(int angle, int speed) {
		Motor.B.setSpeed(speed);
		Motor.B.rotate(angle);
		Motor.B.rotate(-angle);
	}

	public Pilot() {
		super(56, 98, Motor.A, Motor.C, true);
		this.leftSensor = new MyLightSensor("L", SensorPort.S4, 100);
		this.leftSensor.addListener(this);
		this.rightSensor = new MyLightSensor("R", SensorPort.S1, 100);
		this.rightSensor.addListener(this);
	}

	public void lightMeasured(int lightVal, MyLightSensor sensor) {
		
	}

}
