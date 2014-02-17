package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.vision.HSBColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CompoundRobotCluster extends CompoundCluster {

	public CompoundRobotCluster() {
		super(
			"Compound Blue Cluster",
			new HSBCluster[] {
//				new HSBCluster(
//					"blue",
//					new HSBColor(170, 95, 40),
//					new HSBColor(210,100,75),
//					Color.PINK
//				),
				new HSBCluster(
					"dot",
					new HSBColor(130, 40, 25),
					new HSBColor(170, 100, 33),
					Color.PINK
				),
				new HSBCluster(
					"base",
					new HSBColor(100,90,48),
					new HSBColor(170,100,70),
					Color.PINK
				)
			}
		);
	}
	
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(30, 75, 30, 75, 0.3f, 1.1f);
	}

	public List<Vector> getRobotVectors(HSBColor[] hsbArray) {
		List<Vector> lst = new ArrayList<Vector>();
		DotCluster dotCluster = new DotCluster("Dot");
		List<Rect> impRects = this.getImportantRects();
		if (impRects.size() < 2) {
			return null;
		}
		for (Rect impRect : impRects.subList(0, 1)) {
			for (int x = (int) impRect.getX(); x < impRect.getX() + impRect.getWidth(); x++) {
				for (int y = (int) impRect.getY(); y < impRect.getY() + impRect.getHeight(); y++) {
					int index = (int) (y * 640 + x); // SORRY
					index = Math.max(index, 0);
					index = Math.min(index, 640 * 480);
					HSBColor color = hsbArray[index];
					if (!testPixel(x, y, color)) {
						dotCluster.testPixel(x, y, color);
					}
				}
			}
			List<Rect> boundingRects = dotCluster.getImportantRects();
			if (boundingRects.size() > 0) {
				System.out.println(boundingRects.get(0).getCenter());
				System.out.println(impRect.getCenter());
				Vector vec = boundingRects.get(0).getCenter().sub(impRect.getCenter());
				lst.add(vec);
			}
			dotCluster.clear();
		}
		return lst;
	}

}
