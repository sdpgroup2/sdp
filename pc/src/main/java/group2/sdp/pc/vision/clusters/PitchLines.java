package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;

public class PitchLines extends HSBCluster {

	public PitchLines(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,45,100));
	}
	
	public Rect getLinesRect() {
		List<Rect> rects = getRects(100, 500, 500, 1000);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
}
