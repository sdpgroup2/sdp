package sdp.group2.vision;

import sdp.group2.geometry.VecI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * An unconnected graph of pixels where each pixel is a node and there is an
 * edge connecting each pair of adjecent pixels.
 * 
 * @author Paul Harris
 *
 */
public class PixelGraph {
	
	protected Collection<VecI> nodes;
	protected Map<VecI, Set<VecI>> edges;

	public PixelGraph(Collection<VecI> pixels) {
		nodes = pixels;
		edges = new HashMap<VecI, Set<VecI>>();
		for (VecI pixel: pixels) {
			for (VecI offset: VecI.OFFSETS) {
				VecI adj = pixel.add(offset);
				if (pixels.contains(adj)) {
					Set<VecI> neighbours = edges.get(pixel);
					if (neighbours == null) {
						neighbours = new HashSet<VecI>();
						edges.put(pixel, neighbours);
					}
					neighbours.add(adj);
				}
			}
		}
	}
	
	public Collection<VecI> getNodes() {
		return nodes;
	}
	
	public Map<VecI, Set<VecI>> getEdges() {
		return edges;
	}
	
	/**
	 * Returns a set for each region of connected pixels.
	 * @return list of regions of pixels
	 */
	public List<Set<VecI>> getDisjointRegions() {
		List<Set<VecI>> regions = new ArrayList<Set<VecI>>();
		Set<VecI> visitedNodes = new HashSet<VecI>();
		for (VecI node: nodes) {
			if (visitedNodes.contains(node)) {
				continue;
			}
			Set<VecI> region = new HashSet<VecI>();
			Stack<VecI> toVisit = new Stack<VecI>();
			toVisit.add(node);
			while (!toVisit.isEmpty()) {
				VecI next = toVisit.pop();
				region.add(next);
				visitedNodes.add(next);
				Set<VecI> neighbours = edges.get(next);
				if (neighbours != null) {
					for (VecI neighbour: neighbours) {
						if (!visitedNodes.contains(neighbour)) {
							toVisit.add(neighbour);
						}
					}
				}
			}
			regions.add(region);
		}
		return regions;
	}
}
