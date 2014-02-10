package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class BlueRobotCluster extends RobotCluster {
	
	public BlueRobotCluster(String name) {
		super(name, new HSBColor(170, 95, 40), new HSBColor(210,100,75), Color.blue);
	}
}
