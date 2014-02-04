package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;

public class PitchSection extends HSBCluster {

	
	public PitchSection(String name) {
		super(name, new HSBColor(90,35,20), new HSBColor(140,80,40));
	}
		
	public List<Rect> getPitchRects() {
		List<Rect> rects = getRects(0,480, 0, 200);
		if (rects.isEmpty()) {
			return null;
		}
		return rects;
	}
}
