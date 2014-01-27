package group2.sdp.pc.vision;

import java.awt.Color;


/**
 * A cluster for all pixels that could be part of the ball.
 * @author Paul Harris
 */
public class BallCluster extends PixelCluster {
	
	// These values need tuned.
	private static int MIN_RED = 128;
	private static int MAX_GREEN_BLUE = 64;
	
	@Override
	public boolean pixelTest(int x, int y, Color color) {
		return (color.getRed() >= MIN_RED &&
				color.getGreen() <= MAX_GREEN_BLUE &&
				color.getBlue() <= MAX_GREEN_BLUE);
	}

}
