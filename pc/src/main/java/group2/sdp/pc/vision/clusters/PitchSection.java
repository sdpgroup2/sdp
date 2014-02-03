package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;

public class PitchSection extends HSBCluster{

	
	public PitchSection(String name) {
		super(name, new HSBColor(90,35,20), new HSBColor(140,50,40));
	}
		
	public Rect getPitchRect() {
		List<Rect> rects = getRects(50, 200, 200, 500);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
}
