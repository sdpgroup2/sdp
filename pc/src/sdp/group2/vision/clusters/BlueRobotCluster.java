package sdp.group2.vision.clusters;

import sdp.group2.vision.ColorConfig;
import sdp.group2.vision.HSBColor;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class BlueRobotCluster extends RobotCluster {
	
	public BlueRobotCluster(String name) {
		super(name, ColorConfig.BLUE_2_MIN, ColorConfig.BLUE_2_MAX, Color.blue);
	}
}
