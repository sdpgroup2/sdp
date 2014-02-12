package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.util.ColorConfig;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class BlueRobotCluster extends RobotCluster {
	
	public BlueRobotCluster(String name) {
<<<<<<< HEAD
		super(name, ColorConfig.BLUE_2_MIN, ColorConfig.BLUE_2_MAX, Color.blue);
=======
		super(name, new HSBColor(166, 95, 31), new HSBColor(210,100,75), Color.blue);
>>>>>>> milestone3
	}
}
