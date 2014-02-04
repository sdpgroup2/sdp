package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;

public class PitchLines extends HSBCluster {

	public PitchLines(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,45,100), Color.white);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 640, 50, 480);
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
