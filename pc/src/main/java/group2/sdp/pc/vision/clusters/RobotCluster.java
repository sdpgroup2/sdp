package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class RobotCluster extends HSBCluster {
	
	public RobotCluster(String name, HSBColor minColor, HSBColor maxColor, Color debugColor) {
		super(name, minColor, maxColor, debugColor);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(8, 30, 4, 30, 0.5f, 1.1f);
	}

}
