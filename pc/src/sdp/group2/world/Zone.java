package sdp.group2.world;

import sdp.group2.geometry.*;
import sdp.group2.world.Robot;


public class Zone extends Plane {
	private int id;
	private Robot robot;
	
	public Zone(int id) {
		super(Integer.toString(id));
		robot = new Robot();
		this.id = id;
	}
	
	public void updateRobotState(Point p, double theta) {
		robot.updatePosition(p);
	}
	
	public Robot getRobot() {
        return robot;
    }
	
	public Point getRobotPosition() {
        return robot.getPosition();
    }
	
	public int getID() {
		return id;
	}
	
}
