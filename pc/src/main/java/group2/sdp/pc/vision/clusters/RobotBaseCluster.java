package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;

public class RobotBaseCluster extends HSBCluster {

	public RobotBaseCluster(String name) {
		super(name, new HSBColor(100,90,48), new HSBColor(170,100,65), Color.cyan);
	}

}
