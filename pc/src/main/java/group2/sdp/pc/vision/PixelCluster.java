package group2.sdp.pc.vision;

import group2.sdp.pc.geom.VecI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


/**
 * A container for all pixels which pass a certain test, defined by
 * its subclasses.
 * 
 * @author Paul Harris
 *
 */
public abstract class PixelCluster {
	
	protected List<VecI> pixels = new ArrayList<VecI>();

	protected abstract boolean pixelTest(int x, int y, Color color);
	
	
	public void clear() {
		pixels.clear();
	}
	
	public boolean testPixel(int x, int y, Color color) {
		if (pixelTest(x, y, color)) {
			pixels.add(new VecI(x, y));
			return true;
		}
		return false;
	}
	
	public List<VecI> getPixels() {
		return pixels;
	}
	
}
