package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.MathU;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.util.Debug;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompoundCluster extends HSBCluster {

	public HSBCluster[] clusters;
	
	public CompoundCluster(String name, HSBCluster[] clusters) {
		super(name, new HSBColor(0, 0, 0), new HSBColor(0, 0, 0), Color.PINK);
		this.clusters = clusters;
	}
	
	public boolean testPixel(int x, int y, HSBColor color) {
		boolean found = false;
		for (HSBCluster cluster : clusters) {
			found = cluster.testPixel(x, y, color);
			if (found) {
				return found;
			}
		}
		return false;
	}
	
	@Override
	public Set<VecI> getPixels() {
		Set<VecI> vecs = new HashSet<VecI>();
		for (HSBCluster cluster : clusters) {
			vecs.addAll(cluster.getPixels());
		}
		return vecs;
	}
	
	public Point getdotCenter(HSBColor[] hsbArray) {
		List<Rect> rects = this.getImportantRects();
		if (rects.size() < 1) {
			return null;
		}
		Set<VecI> dotSet = new HashSet<VecI>();
		Rect impRect = rects.get(0);
		for (int x = (int) impRect.getX(); x < impRect.getX() + impRect.getWidth(); x++) {
			for (int y = (int) impRect.getHeight(); y < impRect.getY() + impRect.getHeight(); y++) {
				int index = (int) (y * 640 + x); // SORRY
//				HSBColor color = hsbArray[index];
//				if (testPixel(x, y, color)) {
					dotSet.add(new VecI(x, y));
//				}
			}
		}
		dotSet.removeAll(getPixels());
		Point dotCenter = MathU.getBoundingBox(dotSet).getCenter();
		return dotCenter;
	}
	
	public void clear() {
		for (HSBCluster cluster : clusters) {
			cluster.clear();
		}
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(30, 75, 30, 75, 0.3f, 1.1f);
	}
	
	public void setMinColor(HSBColor color) {
	}
	
	public void setMaxColor(HSBColor color) {
	}
	
	public HSBColor getMinColor() {
		return new HSBColor(0, 0, 0);
	}
	
	public HSBColor getMaxColor() {
		return new HSBColor(0, 0, 0);
	}
}
