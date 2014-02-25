package sdp.group2.world;

import sdp.group2.geometry.*;
import sdp.group2.world.Robot;


public class Zone extends Plane {

	Robot robot;
	
	public Zone(byte id) {
		super(Byte.toString(id));
		robot = new Robot();
	}
	
	public void updateRobotState(byte id, Point p, double theta) {
		robot.updatePosition(p);
	}
	
	public Robot getRobot() {
        return robot;
    }
	
	public Point getRobotPosition() {
        return robot.getPosition();
    }
	
}
