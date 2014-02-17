package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.HSBColor;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for detecting robots.
 * @author Jaroslaw Hirniak
 *
 */
public class GrayRobotCluster extends HSBCluster {
	
	public GrayRobotCluster(String name) {
		//super(name, new HSBColor(112, 20, 22), new HSBColor(15, 40, 32), Color.pink);
		super(name, new HSBColor(112, 15, 15), new HSBColor(70, 40, 40), Color.pink);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(2, 20, 2, 20, 0.2f, Float.MAX_VALUE);
	}
}
