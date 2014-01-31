package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.util.List;

public class PitchLines extends AbstractPixelCluster {

	private Color minColor;
	private Color maxColor;
	
	public PitchLines(Color minColor, Color maxColor) {
		this.minColor = minColor;
		this.maxColor = maxColor;
	}
	
	@Override
	public boolean colorTest(int x, int y, Color color) {
		return (minColor.getRed() <= color.getRed() && maxColor.getRed() >= color.getRed() &&
				minColor.getGreen() <= color.getGreen() && maxColor.getGreen() >= color.getGreen() &&
				minColor.getBlue() <= color.getBlue() && maxColor.getBlue() >= color.getBlue());
	}
	
	
	public Rect getLinesRect() {
		List<Rect> rects = getRects(100, 500, 500, 1000);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
}
