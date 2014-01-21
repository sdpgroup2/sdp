package group2.sdp.robot;

import lejos.nxt.Button;

public class App {

	public static void main(String[] args) {
		Pilot pilot = new Pilot();
		pilot.moveForward(5);
		Button.waitForAnyPress();
	}

}
