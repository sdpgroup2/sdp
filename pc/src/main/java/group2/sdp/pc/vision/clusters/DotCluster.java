package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;


public class DotCluster extends HSBCluster {

	public DotCluster(String name) {
		super(name, ColorConfig.LINES_2_MIN, ColorConfig.DOT_2_MAX, Color.ORANGE);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(3, 10, 3, 10, 0.5f, 1.1f);
	}
	
}
