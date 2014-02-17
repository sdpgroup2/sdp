package sdp.group2.vision.clusters;

import sdp.group2.geometry.VecI;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


public abstract class AbstractRGBCluster extends AbstractPixelCluster<Color> {
	
	protected Set<VecI> pixels = new HashSet<VecI>();

	
	public AbstractRGBCluster() {
		super("RGB Cluster");
	}
	
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
}
