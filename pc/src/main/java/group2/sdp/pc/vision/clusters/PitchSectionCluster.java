package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class PitchSectionCluster extends HSBCluster {

	
	public PitchSectionCluster(String name) {
		super(name, ColorConfig.PITCH_1_MIN, ColorConfig.PITCH_1_MAX, Color.green);
	}
		
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 480, 50, 200);
	}
}
