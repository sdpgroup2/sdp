package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.ArrayList;
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
		return getRects(8, 20, 4, 20, 0.5f, 1.1f);
	}

	public List<Vector> getRobotVectors(HSBColor[] hsbArray) {
		List<Vector> lst = new ArrayList<Vector>();
		DotCluster cluster = new DotCluster("Dot");
		for (Rect rect : getImportantRects().subList(0, 2)) {
			Rect expandedRect = rect.expand(2);
			for (int x = (int) rect.getX(); x < rect.getX() + rect.getWidth(); x++) {
				for (int y = (int) rect.getY(); y < rect.getY() + rect.getHeight(); y++) {
					int index = y * 640 + x; // SORRY
					HSBColor color = hsbArray[index];
					cluster.testPixel(x, y, color);
				}

			}
			Vector vec = rect.getCenter().sub(cluster.getRects().get(0).getCenter());
			lst.add(vec);
			cluster.clear();

		}
		return lst;
	}

}
