package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class PitchSection extends HSBCluster {

	
	public PitchSection(String name) {
		super(name, new HSBColor(90,35,20), new HSBColor(140,80,40), Color.green);
	}
		
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 480, 50, 200);
	}
}
