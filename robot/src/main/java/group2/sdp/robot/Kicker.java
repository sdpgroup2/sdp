package group2.sdp.robot;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;

public class Kicker extends DifferentialPilot{
	public Kicker() {
		super(Pilot.WHEEL_SIZE_NXT2, 4.4f, Motor.C, Motor.A, false);
		
	}
}
