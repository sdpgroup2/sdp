package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.util.List;

public class PitchLines extends AbstractPixelCluster {

	private static int MIN_RED  = 80;
	private static int MIN_BLUE = 40;
	private static int MIN_GREEN= 40;
	
	@Override
	public boolean colorTest(int x, int y, Color color) {
		return (color.getRed() >= MIN_RED &&
				color.getGreen() >= MIN_GREEN &&
				color.getBlue() <= MIN_BLUE);
	}
	
	
	public Rect getLinesRect() {
		List<Rect> rects = getRects(100, 500, 500, 1000);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
}
