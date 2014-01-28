package group2.sdp.pc.vision;

import group2.sdp.pc.geom.VecI;

import java.awt.Color;
import java.util.Collection;


/**
 * A container for all pixels which pass a certain test.
 * 
 * @author Paul Harris
 */
public interface PixelCluster {

	public boolean testPixel(int x, int y, Color color);
	public void clear();
	public Collection<VecI> getPixels();
	
}
