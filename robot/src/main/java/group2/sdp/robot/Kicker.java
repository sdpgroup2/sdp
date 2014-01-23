package group2.sdp.robot;

import lejos.nxt.Motor;

public class Kicker {
	public static void kick( int angle, int speed) {
		Motor.B.setSpeed(speed);
		Motor.B.rotate(angle);
		Motor.B.rotate(-angle);
	}
}
