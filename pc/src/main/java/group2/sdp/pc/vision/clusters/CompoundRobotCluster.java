package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;

public class CompoundRobotCluster extends CompoundCluster {

	public CompoundRobotCluster() {
		super(
			"Compound Blue Cluster",
			new HSBCluster[] {
				new HSBCluster(
					"blue",
					new HSBColor(170, 95, 40),
					new HSBColor(210,100,75),
					Color.PINK
				),
//				new HSBCluster(
//					"dot",
//					new HSBColor(130, 40, 25),
//					new HSBColor(170, 100, 33),
//					Color.PINK
//				),
				new HSBCluster(
					"base",
					new HSBColor(100,90,48),
					new HSBColor(170,100,70),
					Color.PINK
				)
			}
		);
	}

}
