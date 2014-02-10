package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class YellowRobotCluster extends RobotCluster {
	
	public YellowRobotCluster(String name) {
		super(name, new HSBColor(25,55,50), new HSBColor(40,100,90), Color.yellow);
	}
}
