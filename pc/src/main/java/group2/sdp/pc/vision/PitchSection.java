package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.util.List;

public class PitchSection extends AbstractPixelCluster{

	private static int MIN_GREEN= 80;
	private static int MAX_RED_BLUE = 40;
	
	@Override
	public boolean colorTest(int x, int y, Color color) {
		return (color.getRed() >= MIN_GREEN &&
				color.getGreen() >= MAX_RED_BLUE &&
				color.getBlue() <= MAX_RED_BLUE);
	}
	
	
	public Rect getPitchRect() {
		List<Rect> rects = getRects(50, 200, 200, 500);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
}
