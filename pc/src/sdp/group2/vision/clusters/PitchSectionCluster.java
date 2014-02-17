package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.ColorConfig;
import sdp.group2.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class PitchSectionCluster extends HSBCluster {

	
	public PitchSectionCluster(String name) {
		super(name, ColorConfig.PITCH_2_MIN, ColorConfig.PITCH_2_MAX, Color.green);
	}
		
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 480, 50, 200);
	}
}
