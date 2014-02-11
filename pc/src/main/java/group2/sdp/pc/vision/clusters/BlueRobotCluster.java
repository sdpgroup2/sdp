package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class BlueRobotCluster extends RobotCluster {
	
	public BlueRobotCluster(String name) {
		super(name, ColorConfig.BLUE_1_MIN, ColorConfig.BLUE_1_MAX, Color.blue);
	}
}
