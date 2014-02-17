package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.VecI;

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
