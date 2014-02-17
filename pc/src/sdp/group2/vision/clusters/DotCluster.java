package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.ColorConfig;

import java.awt.Color;
import java.util.List;


public class DotCluster extends HSBCluster {

	public DotCluster(String name) {
		super(name, ColorConfig.DOT_2_MIN, ColorConfig.DOT_2_MAX, Color.ORANGE);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(6, 11, 6, 11, 0.5f, 1.1f);
	}
	
}
