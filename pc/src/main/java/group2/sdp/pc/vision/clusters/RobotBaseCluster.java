package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class RobotBaseCluster extends HSBCluster {

	public RobotBaseCluster(String name) {
		super(name, new HSBColor(100,90,48), new HSBColor(170,100,70), Color.cyan);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(30, 75, 30, 75, 0.3f, 1.1f);
	} 

	public Vector getRobotVector(HSBColor[] hsbArray) {
		DotCluster dotCluster = new DotCluster("Dot");
		List<Rect> impRects = this.getImportantRects();
		if (impRects.size() < 1) {
			return null;
		}
		Rect impRect = impRects.get(0);
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
			return vec;
		}
		return null;
	}
	
}
