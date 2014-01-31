package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for detecting robots.
 * @author Paul Harris
 *
 */
public class RobotCluster extends AbstractPixelCluster {
	
	private Color minColor;
	private Color maxColor;
	
	public RobotCluster(Color minColor, Color maxColor) {
		this.minColor = minColor;
		this.maxColor = maxColor;
	}
	
	@Override
	protected boolean colorTest(int x, int y, Color color) {
		return (minColor.getRed() <= color.getRed() && color.getRed() <= maxColor.getRed() &&
				minColor.getGreen() <= color.getGreen() && color.getGreen() <= maxColor.getGreen() &&
				minColor.getBlue() <= color.getBlue() && color.getBlue() <= maxColor.getBlue());
	}
	
	public List<Rect> getRobotRects() {
		return getRects(10, 25, 10, 25, 0.4f, 1.0f);
	}

}
