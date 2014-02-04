package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;

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

}
