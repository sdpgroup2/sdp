package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class RobotBaseCluster extends HSBCluster {

	public RobotBaseCluster(String name) {
		super(name, new HSBColor(100,90,48), new HSBColor(170,100,70), Color.cyan);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(30, 75, 30, 75, 0.3f, 1.1f);
	} 

}
