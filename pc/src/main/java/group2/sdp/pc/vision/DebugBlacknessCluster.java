package group2.sdp.pc.vision;

import java.awt.Color;

/**
 * Clusters all black pixels.
 * @author Paul Harris
 *
 */
public class DebugBlacknessCluster extends PixelCluster {

	private static int THRESHOLD = 35;
	
	@Override
	public boolean pixelTest(int x, int y, Color color) {
		return (color.getRed() < THRESHOLD && color.getGreen() < THRESHOLD && color.getBlue() < THRESHOLD);
	}
	
}
