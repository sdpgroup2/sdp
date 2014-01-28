package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for all pixels that could be part of the ball.
 * @author Paul Harris
 */
public class BallCluster extends AbstractPixelCluster {
	
	private static int MIN_RED = 80;
	private static int MAX_GREEN_BLUE = 40;
	
	
	@Override
	public boolean colorTest(int x, int y, Color color) {
		return (color.getRed() >= MIN_RED &&
				color.getGreen() <= MAX_GREEN_BLUE &&
				color.getBlue() <= MAX_GREEN_BLUE);
	}
	
	public Rect getBallRect() {
		List<Rect> rects = getRects(8, 24, 8, 24);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}

}
