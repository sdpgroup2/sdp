package group2.sdp.robot;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.util.TextMenu;

public class App {

	public static final int ROTATE_RIGHT = 1;
	public static final int ROTATE_LEFT = -1;
	public static final int ATTACKER_FIELD = 4700;
	public static final int DEFENDER_FIELD = 3900;
	public static final int COLOR_THRESHOLD = 37;

	public static void main(String[] args) {
		Pilot pilot;
		int priorDistance;
		int direction;
		int distance;

		// Menu which allows selecting the position
		String[] menuItems = { "Attack", "Defence" };
		TextMenu textMenu = new TextMenu(menuItems, 1, "Select Position:");
		distance = textMenu.select() == 0 ? ATTACKER_FIELD : DEFENDER_FIELD;
		LCD.clear();

		// Move forward until you hit white line
		pilot = new Pilot();
		pilot.moveForward(100);

		while (true) {
			if (pilot.leftSensor.readValue() > COLOR_THRESHOLD) {
				direction = ROTATE_RIGHT;
				break;
			} else if (pilot.rightSensor.readValue() > COLOR_THRESHOLD) {
				direction = ROTATE_LEFT;
				break;
			}

		}

		// Start going around
		int rightLightVal;
		int leftLightVal;
		priorDistance = (Motor.A.getTachoCount() + Motor.C.getTachoCount()) / 2;
		while (true) {
			rightLightVal = pilot.rightSensor.readValue();
			leftLightVal = pilot.leftSensor.readValue();
			if (rightLightVal > COLOR_THRESHOLD || leftLightVal > COLOR_THRESHOLD) {
				// One sensor sees white, rotate accordingly
				if (direction == ROTATE_RIGHT) {
					pilot.rotateRight();
				} else {
					pilot.rotateLeft();
				}
			} else if (leftLightVal < COLOR_THRESHOLD && rightLightVal < COLOR_THRESHOLD) {
				// Slightly steer towards the line
				pilot.steer(direction * 3);
			} else if (((Motor.A.getTachoCount() + Motor.C.getTachoCount()) / 2) - priorDistance < -distance) {
				// Reached the starting point
				pilot.stop();
				break;
			}
		}
	}
}