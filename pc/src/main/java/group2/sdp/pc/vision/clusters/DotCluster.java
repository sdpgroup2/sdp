package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;


public class DotCluster extends HSBCluster {

	public DotCluster(String name) {
		super(name, new HSBColor(38, 15, 15), new HSBColor(130, 33, 33), Color.ORANGE);
//		super(name, new HSBColor(112, 20, 22), new HSBColor(15, 33, 33), Color.ORANGE);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(2, 10, 2, 10, 0.5f, Float.MAX_VALUE);
	}
	
}
