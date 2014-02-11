package group2.sdp.pc.strategy;

import group2.sdp.pc.geom.*;

public class Zone extends Plane {

	Robot robot;
	
	public Zone(byte id)
	{ 
		super(Byte.toString(id));
		robot = new Robot();
	}
	
	public void updateRobotState(byte id, Point p, double theta) {
		robot.updatePoisition(p);
	}
	
}
