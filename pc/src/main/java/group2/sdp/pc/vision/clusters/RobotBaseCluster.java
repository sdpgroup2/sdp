package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;

public class RobotBaseCluster extends HSBCluster {
	
	public RobotBaseCluster(String name) {
		super(name, new HSBColor(0,0,0), new HSBColor(0,0,0), Color.cyan);
	}

}
