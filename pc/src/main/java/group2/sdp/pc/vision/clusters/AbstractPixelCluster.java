package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.MathU;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.PixelGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractPixelCluster<T> implements PixelCluster<T> {

	protected Set<VecI> pixels = new HashSet<VecI>();
	protected String name;
	protected List<Set<VecI>> _regions;
	protected boolean splitThisFrame = false;

	/**
	 * Does the cluster contain any pixels?
	 * @return The emptiness of the cluster
	 */
	public boolean isClear() {
		return this.pixels.isEmpty();
	}
	
	public AbstractPixelCluster(String name) {
		this.name = name;
	}

	@Override
	public void clear() {
		pixels.clear();
		splitThisFrame = false;
	}

	@Override
	public Set<VecI> getPixels() {
		return pixels;
	}

	@Override
	public String toString() {
		return name;
	}

	public List<Set<VecI>> getRegions() {
		if (splitThisFrame) {
			return _regions;
		} else {
			PixelGraph graph = new PixelGraph(getPixels());
			_regions = graph.getDisjointRegions();
			splitThisFrame = true;
			return _regions;
		}
	}

	/**
	 * Get the bounding rectangle of all joined regions of pixels in this cluster.
	 * The parameters filter out rectangles which are too big or too small.
	 * Length refers to the longest edge and breadth to the shortest.
	 * The minFill and maxFill parameters refer to how much of the rect is filled as
	 * a float between 0 and 1.
	 */
	public List<Rect> getRects(
			int minLength, int maxLength, int minBreadth, int maxBreadth, float minFill, float maxFill) {
		List<Rect> rects = new ArrayList<Rect>();
		for (Set<VecI> region: getRegions()) {
			Rect rect = MathU.getBoundingBox(region);
			int rectArea = (int) (rect.getWidth() * rect.getHeight());
			double fill = (rectArea > 0) ? (region.size()) / (rect.getWidth() * rect.getHeight()) : 0f;
			int length = (int) Math.max(rect.getWidth(), rect.getHeight());
			int breadth = (int) Math.min(rect.getWidth(), rect.getHeight());
			if (minLength <= length && length <= maxLength &&
					minBreadth <= breadth && breadth <= maxBreadth &&
					minFill <= fill && fill <= maxFill) {
				rects.add(rect);
			}
		}
		return rects;
	}
	
	/**
	 * HACK function
	 * @param minLength
	 * @param maxLength
	 * @param minBreadth
	 * @param maxBreadth
	 * @param minFill
	 * @param maxFill
	 * @param robotCluster
	 * @return
	 */
	public List<Rect> getRects(int minLength, int maxLength, int minBreadth,
			int maxBreadth, float minFill, float maxFill, HSBCluster robotCluster) {
		List<Rect> rects = new ArrayList<Rect>();
		for (Set<VecI> region: getRegions()) {
			Rect rect = MathU.getBoundingBox(region);
			int rectArea = (int) (rect.getWidth() * rect.getHeight());
			double fill = (rectArea > 0) ? (region.size()) / (rect.getWidth() * rect.getHeight()) : 0f;
			int length = (int) Math.max(rect.getWidth(), rect.getHeight());
			int breadth = (int) Math.min(rect.getWidth(), rect.getHeight());
			List<Rect> robotclusterImpRects = robotCluster.getImportantRects();
			if (minLength <= length && length <= maxLength &&
					minBreadth <= breadth && breadth <= maxBreadth &&
					minFill <= fill && fill <= maxFill) {
				if (robotclusterImpRects == null || robotclusterImpRects.size() > 0) {
					if (rect.contains(robotclusterImpRects.get(0))) {
						rects.add(rect);
					}
				}
			}
		}
		return rects;
	}

	public List<Rect> getRects(int minLength, int maxLength, int minBreadth, int maxBreadth) {
		return getRects(minLength, maxLength, minBreadth, maxBreadth, 0, 1);
	}

	public List<Rect> getRects() {
		return getRects(0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, 0.0f, 2.0f);
	}

	/**
	 *
	 * returns the cluster with the largest number of pixels
	 */
	public Set<VecI> getLargestRegion() {
		int max = 0;
		Set<VecI> largestRegion = new HashSet<VecI>();
		for (Set<VecI> region : getRegions()) {
			if (region.size() > max) {
				max = region.size();
				largestRegion = region;
			}
		}

		return largestRegion;
	}

	public List<Rect> getImportantRects() {
		return getRects();
	}

}
