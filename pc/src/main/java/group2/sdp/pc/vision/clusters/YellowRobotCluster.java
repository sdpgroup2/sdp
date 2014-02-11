package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class YellowRobotCluster extends RobotCluster {
	
	public YellowRobotCluster(String name) {
		super(name, ColorConfig.YELLOW_1_MIN, ColorConfig.YELLOW_1_MAX, Color.yellow);
	}
}
