package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class RobotBaseCluster extends HSBCluster {

	public RobotBaseCluster(String name) {
		super(name, ColorConfig.BASE_2_MIN, ColorConfig.BASE_2_MAX, Color.cyan);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(25, 75, 25, 75, 0.5f, 1.1f);
	}
	
	public List<Rect> getImportantRects(RobotCluster robotCluster) {
		return getRects(40, 75, 40, 75, 0.3f, 1.1f, robotCluster);
	} 

	public Vector getRobotVector(HSBColor[] hsbArray, RobotCluster robotCluster) {
		DotCluster dotCluster = new DotCluster("Dot");
		List<Rect> impRects = this.getImportantRects(robotCluster);
		if (impRects.size() < 1) {
			return null;
		}
		Rect impRect = impRects.get(0);
		for (int x = (int) impRect.getX(); x < impRect.getX() + impRect.getWidth(); x++) {
			for (int y = (int) impRect.getY(); y < impRect.getY( ) + impRect.getHeight(); y++) {
				int index = (int) (y * 640 + x); // SORRY
				index = Math.max(index, 0);
				index = Math.min(index, 640 * 480);
				HSBColor color = hsbArray[index];
				if (!testPixel(x, y, color)) {
					dotCluster.testPixel(x, y, color);
					robotCluster.testPixel(x, y, color);
				}
			}
		}
		List<Rect> dotBoundingRects = dotCluster.getImportantRects();
		List<Rect> robotBoundingRects = robotCluster.getImportantRects();
		if (dotBoundingRects.size() > 0 && robotBoundingRects.size() > 0) {
			Point dotCenter = dotBoundingRects.get(0).getCenter();
			Point robotCenter = robotBoundingRects.get(0).getCenter();
			Vector vec = dotCenter.sub(robotCenter);
			vec.negate();
			return vec;
		}
		return null;
	}
	
}
