package sdp.group2.vision.clusters;

import sdp.group2.geometry.VecI;
import sdp.group2.vision.HSBColor;

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

	/*
	
	public VecI getEdgePixel(int index, Dir edge) {
		Set<VecI> pixels = getLargestRegion();
		int finalX = 0;
		int finalY = 0;
		
		VecI finalPoint = new VecI(0,0);
		
		switch(edge) {
		case Left   : finalX = 1000; break;
		case Up     : finalY = 1000; break;
		}
		
		for(VecI pixel : pixels)
		{
			switch(edge) {
				case Left : {
					if(pixel.x < finalX && pixel.y == index)
						finalPoint = pixel; 
					break;
					}
				case Right : {
					if(pixel.x > finalX && pixel.y == index)
						finalPoint = pixel; 
					break;
					}
				case Up : {
					if(pixel.y < finalY && pixel.x == index)
						finalPoint = pixel; 
					break;
					}
				case Down : {
					if(pixel.y > finalY && pixel.x == index)
						finalPoint = pixel; 
					break;
					}
				}
			}

		return finalPoint;
	}
	
	public VecI getEdgePixel(Dir edge) {
		Set<VecI> pixels = getLargestRegion();
		VecI finalPoint = new VecI(0,0);
		
		int finalX = 0;
		int finalY = 0;
		
		switch(edge) {
		case Left : finalX = 1000; break;
		case Up   : finalY = 1000; break;
		}
		
		for(VecI pixel : pixels)
		{
			switch(edge) {
				case Left : {
					if(pixel.x < finalX)
						finalPoint = pixel; 
					break;
					}
				case Right : {
					if(pixel.x > finalX)
						finalPoint = pixel; 
					break;
					}
				case Up : {
					if(pixel.y < finalY)
						finalPoint = pixel; 
					break;
					}
				case Down : {
					if(pixel.y > finalY)
						finalPoint = pixel; 
					break;
					}
				}
			}
		return finalPoint;
	}

*/

}
