package group2.sdp.pc.vision;

import group2.sdp.pc.geom.GeomUtil;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractPixelCluster implements PixelCluster {
	
	protected Set<VecI> pixels = new HashSet<VecI>();

	protected abstract boolean colorTest(int x, int y, Color color);
	
	public void clear() {
		pixels.clear();
	}
	
	public boolean testPixel(int x, int y, Color color) {
		if (colorTest(x, y, color)) {
			pixels.add(new VecI(x, y));
			return true;
		}
		return false;
	}
	
	public Set<VecI> getPixels() {
		return pixels;
	}
	
	/**
	 * Get the bounding rectangle of all joined regions of pixels in this cluster.
	 * The parameters filter out rectangles which are too big or too small.
	 * The minFill and maxFill parameters refer to how much of the rect is filled as
	 * a float between 0 and 1. 
	 */
	public List<Rect> getRects(
			int minWidth, int maxWidth, int minHeight, int maxHeight, float minFill, float maxFill) {
		PixelGraph graph = new PixelGraph(pixels);
		List<Rect> rects = new ArrayList<Rect>();
		for (Set<VecI> region: graph.getDisjointRegions()) {
			Rect rect = GeomUtil.getBoundingBox(region);
			int rectArea = (rect.width*rect.height);
			float fill = (rectArea > 0) ? ((float) region.size()) / (rect.width*rect.height) : 0f;
			if (minWidth <= rect.width && rect.width <= maxWidth &&
					minHeight <= rect.height && rect.height <= maxHeight &&
					minFill <= fill && fill <= maxFill) {
				rects.add(rect);
			}
		}
		return rects;
	}
	
	public List<Rect> getRects(int minWidth, int maxWidth, int minHeight, int maxHeight) {
		return getRects(minWidth, maxWidth, minHeight, maxHeight, 0, 1);
	}
	
	public List<Rect> getRects() {
		return getRects(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0, 1);
	}
	
}
