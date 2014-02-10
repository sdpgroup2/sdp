package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class CompoundCluster extends RobotCluster {

	protected List<HSBColor[]> ranges;
	public final Color debugColor;
	
	public CompoundCluster(String name, List<HSBColor[]> ranges, Color debugColor) {
		super(name, new HSBColor(155, 44, 30), new HSBColor(206,63,60), Color.PINK);
		this.ranges = ranges;
		this.debugColor = debugColor;
	}
	
	public boolean testPixel(int x, int y, HSBColor color) {
		for (HSBColor[] range : ranges) {
			if (HSBColor.inRange(color, range[0], range[1])) {
				pixels.add(new VecI(x, y));
				return true;
			}
		}
		return false;
	}
	
	public void setMinColor(HSBColor color) {
	}
	
	public void setMaxColor(HSBColor color) {
	}
	
	public HSBColor getMinColor() {
		return null;
	}
	
	public HSBColor getMaxColor() {
		return null;
	}
}
