package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CompoundRobotCluster extends CompoundCluster {

	public CompoundRobotCluster() {
		super(
			"Compound Blue Cluster",
			new ArrayList<HSBColor[]>() {{
				add(new HSBColor[] {
					new HSBColor(155, 44, 30),
					new HSBColor(206, 63, 60)
				});
				add(new HSBColor[] {
					new HSBColor(136, 66, 28),
					new HSBColor(174, 91, 40)
				});
			}},
			Color.PINK
		);
	}

}
