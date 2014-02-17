package sdp.group2.geometry;

import java.util.Collection;

public class MathU {

	/**
	 * 
	 * @param pixels
	 * @return      		The rectangle coordinates which form
	 * 						the boundary for the cluster of pixels      
	 */
	public static Rect getBoundingBox(Collection<VecI> pixels) {
		if (pixels.size() == 0) {
			return null;
		}
		boolean initialized = false;
		int minX = 0;
		int maxX = 0;	
		int minY = 0;
		int maxY = 0;
		for (VecI pixel: pixels) {
			if (!initialized) {
				minX = pixel.x;
				maxX = pixel.x;
				minY = pixel.y;
				maxY = pixel.y;
				initialized = true;
				continue;
			}
			if (pixel.x < minX) {
				minX = pixel.x;
			} else if (pixel.x > maxX) {
				maxX = pixel.x;
			}
			if (pixel.y < minY) {
				minY = pixel.y;
			} else if (pixel.y > maxY) {
				maxY = pixel.y;
			}
		}
		// Adding one below because 521-520 is 2 units not 1
		Rect rect = new Rect(minX, minY, maxX - minX + 1, maxY - minY + 1);
		return rect;
	}
	
	/**
	 * Clamps a value between 0 and 1.
	 * @return
	 */
	public static float clamp(float value) {
		return clamp(value, 0, 1);
	}
	
	/**
	 * Clamp one value between a min and a max
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(value, max));
	}
}
