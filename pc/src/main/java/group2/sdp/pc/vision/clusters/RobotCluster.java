package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class RobotCluster extends HSBCluster {
	
	public RobotCluster(String name, HSBColor minColor, HSBColor maxColor) {
		super(name, minColor, maxColor);
	}

	public List<Rect> getRobotRects() {
		return getRects(8, 20, 4, 20, 0.5f, 1.1f);
	}

}
