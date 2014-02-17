package sdp.group2.vision.clusters;

import sdp.group2.geometry.VecI;

import java.util.Collection;


/**
 * A container for all pixels which pass a certain test.
 * 
 * @author Paul Harris
 */
public interface PixelCluster<T> {

	public boolean testPixel(int x, int y, T color);
	public void clear();
	public Collection<VecI> getPixels();
	
}
