package group2.sdp.pc.geom;

import java.util.Collection;

public class GeomUtil {

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
		return new Rect(minX, maxX, minY, maxY);
	}	
}
