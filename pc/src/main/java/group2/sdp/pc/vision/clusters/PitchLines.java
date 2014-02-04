package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.geom.Shape;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;

public class PitchLines extends HSBCluster {

	public PitchLines(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,45,100));
	}
	
	public Rect getLinesRect() {
		List<Rect> rects = getRects(100, 640,50, 480);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}
	
	public VecI getCorners() {
		
		 VecI leftmostPixel = super.getMinXPixel();		 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition - 10);
				 break;
			 }
			 yposition--;
		 }
		 
		 return topCorner;
		
	}
}
