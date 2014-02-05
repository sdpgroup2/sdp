package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.Set;



public class HSBCluster extends AbstractPixelCluster<HSBColor> {

	protected HSBColor minColor;
	protected HSBColor maxColor;
	public final Color debugColor;
	
	public HSBCluster(String name, HSBColor minColor, HSBColor maxColor, Color debugColor) {
		super(name);
		this.minColor = minColor;
		this.maxColor = maxColor;
		this.debugColor = debugColor;
	}
	
	public boolean testPixel(int x, int y, HSBColor color) {
		if (HSBColor.inRange(color, minColor, maxColor)) {
			pixels.add(new VecI(x, y));
			return true;
		}
		return false;
	}
	
	public void setMinColor(HSBColor color) {
		minColor = color;
	}
	
	public void setMaxColor(HSBColor color) {
		maxColor = color;
	}
	
	public HSBColor getMinColor() {
		return minColor;
	}
	
	public HSBColor getMaxColor() {
		return maxColor;
	}
	
	/**
	 * 
	 * A set of functions for finding a pixel on the edge of a cluster.
	 * 
	 * @param An x or y coordinate
	 * @return The (left/right/upper/bottom)most pixel in the row corresponding to
	 * the input x or y coordinate
	 */

	public VecI getMaxXPixel(int y) {
		Set<VecI> pixels = getLargestRegion();
		VecI max = new VecI(0,0);
		
		for(VecI pixel : pixels)
		{
			if(pixel.x > max.x && pixel.y == y)
				max = pixel;
		}
		
		return max;
	}

	public VecI getMaxYPixel(int x) {
	    Set<VecI> pixels = getLargestRegion();
		VecI max = new VecI(0,0);
		
		for(VecI pixel : pixels)
		{
			if(pixel.y > max.y && pixel.x == x)
				max = pixel;
		}
		
		return max;
	}

	public VecI getMinXPixel(int y) {
		Set<VecI> pixels = getLargestRegion();
		VecI min = new VecI(1000,0);
	
		for(VecI pixel : pixels)
		{
			if(pixel.x < min.x && pixel.y == y)
				min = pixel;
		}
	
		return min;
	}

	public VecI getMinYPixel(int x) {
		Set<VecI> pixels = getLargestRegion();
		VecI min = new VecI(0,1000);
	
		for(VecI pixel : pixels)
		{
			if(pixel.x < min.x && pixel.x == x)
				min = pixel;
		}
	
		return min;
	}
	
	/**
	 * A set of functions for finding a pixel on the edge of a cluster.
	 * 	
	 * @return The (left/right/upper/bottom)most pixel in the largest cluster.
	 */
	
	public VecI getMaxXPixel() {
		Set<VecI> pixels = getLargestRegion();
		VecI max = new VecI(0,0);
		
		for(VecI pixel : pixels)
		{
			if(pixel.x > max.x)
				max = pixel;
		}
		
		return max;
	}

	public VecI getMaxYPixel() {
	    Set<VecI> pixels = getLargestRegion();
		VecI max = new VecI(0,0);
		
		for(VecI pixel : pixels)
		{
			if(pixel.y > max.y)
				max = pixel;
		}
		
		return max;
	}

	public VecI getMinXPixel() {
		Set<VecI> pixels = getLargestRegion();
		VecI min = new VecI(1000,0);
	
		for(VecI pixel : pixels)
		{
			if(pixel.x < min.x)
				min = pixel;
		}
	
		return min;
	}

	public VecI getMinYPixel() {
		Set<VecI> pixels = getLargestRegion();
		VecI min = new VecI(0,1000);
	
		for(VecI pixel : pixels)
		{
			if(pixel.x < min.x)
				min = pixel;
		}
	
		return min;
	}


}
