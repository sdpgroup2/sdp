package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;


public class DotCluster extends HSBCluster {

	public DotCluster(String name) {
		super(name, new HSBColor(60, 20, 20), new HSBColor(120, 38, 38), Color.magenta);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(2, 20, 2, 20, 0.5f, 1.1f);
	}

}
