package group2.sdp.robot;

import group2.sdp.robot.light.LightListener;
import group2.sdp.robot.light.MyLightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

public class Pilot extends DifferentialPilot implements LightListener {

	public MyLightSensor lightSensor;
	public static int LightTreshhold = 0;
	public static int WHITE_THRESHOLD = 37;

	public void rotate(int direction, double weight) {
	}

	public void move(double direction) {
	}

	public void moveForward(double speed) {
		this.setTravelSpeed(speed);
		this.forward();
	}

	public Pilot() {
		super(Pilot.WHEEL_SIZE_NXT2, 4.4f, Motor.C, Motor.A, false);
		this.lightSensor = new MyLightSensor(SensorPort.S1, 100);
		this.lightSensor.addListener(this);
	}

	public void lightMeasured(int lightVal, MyLightSensor sensor) {
		System.out.println(lightVal);
		if (lightVal > WHITE_THRESHOLD) {
			System.out.println(lightVal);
			this.stop();
		}
	}

}