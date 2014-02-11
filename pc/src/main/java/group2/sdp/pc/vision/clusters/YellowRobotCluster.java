package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.util.ColorConfig;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class YellowRobotCluster extends RobotCluster {
	
	public YellowRobotCluster(String name) {
		super(name, ColorConfig.YELLOW_2_MIN, ColorConfig.YELLOW_2_MAX, Color.yellow);
	}
}
