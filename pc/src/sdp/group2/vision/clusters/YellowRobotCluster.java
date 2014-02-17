package sdp.group2.vision.clusters;

import sdp.group2.vision.ColorConfig;
import sdp.group2.vision.HSBColor;

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
