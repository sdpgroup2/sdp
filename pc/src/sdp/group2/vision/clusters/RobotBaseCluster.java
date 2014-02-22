package sdp.group2.vision.clusters;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.vision.ColorConfig;
import sdp.group2.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class RobotBaseCluster extends HSBCluster {

	public RobotBaseCluster(String name) {
		super(name, ColorConfig.BASE_2_MIN, ColorConfig.BASE_2_MAX, Color.cyan);
	}

	@Override
	public List<Rect> getImportantRects() {
		return getRects(25, 75, 25, 75, 0.3f, 1.1f);
	} 

	public Vector getRobotVector(HSBColor[] hsbArray) {
		DotCluster dotCluster = new DotCluster("Dot");
		YellowRobotCluster robotCluster = new YellowRobotCluster("Blue robot");
		List<Rect> impRects = this.getImportantRects();
		if (impRects.size() < 1) {
			System.out.println("Missing the base plates!");
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