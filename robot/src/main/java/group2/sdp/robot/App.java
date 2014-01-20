package group2.sdp.robot;

import lejos.nxt.Button;

public class App {

	public static void main(String[] args) {
		System.out.println("Hello World!");
		Pilot pilot = new Pilot();
		pilot.moveForward(5);
		Button.waitForAnyPress();
	}

}
