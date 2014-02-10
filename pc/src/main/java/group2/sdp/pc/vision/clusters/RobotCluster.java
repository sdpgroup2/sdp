package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.MathU;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
		return getRects(8, 20, 6, 20, 0.5f, 1.1f);
	}

	public List<Vector> getRobotVectors(HSBColor[] hsbArray) {
		List<Vector> lst = new ArrayList<Vector>();
		DotCluster dotCluster = new DotCluster("Dot");
		List<Set<VecI>> regions = this.getRegions();
		if (regions.size() < 2) {
			return null;
		}
		for (Set<VecI> reg : regions.subList(0, 2)) {
			System.out.println(reg);
			Rect rect = MathU.getBoundingBox(reg);
			System.out.println(rect);
			Rect expandedRect = rect.expand(2);
			for (int x = (int) expandedRect.getX(); x < expandedRect.getX() + expandedRect.getWidth(); x++) {
				for (int y = (int) expandedRect.getY(); y < expandedRect.getY() + expandedRect.getHeight(); y++) {
					int index = (int) (y * 640 + x); // SORRY
					index = Math.max(index, 0);
					index = Math.min(index, 640*480);
					HSBColor color = hsbArray[index];
					dotCluster.testPixel(x, y, color);
				}
			}
			List<Rect> boundingRects = dotCluster.getImportantRects();
			if (boundingRects.size() > 0) {
				Vector vec = (boundingRects.get(0).getCenter().sub(rect.getCenter()));
				lst.add(vec);
			}
			dotCluster.clear();
		}
		return lst;
	}

}
