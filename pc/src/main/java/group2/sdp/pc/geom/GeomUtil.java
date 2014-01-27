package group2.sdp.pc.geom;

import java.util.List;

public class GeomUtil {

	public static Rect getBoundingBox(List<VecI> pixels) {
		if (pixels.size() == 0) {
			return null;
		}
		VecI first = pixels.get(0);
		int minX = first.x;
		int maxX = first.x;		
		int minY = first.y;
		int maxY = first.y;
		for (VecI pixel: pixels) {
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
