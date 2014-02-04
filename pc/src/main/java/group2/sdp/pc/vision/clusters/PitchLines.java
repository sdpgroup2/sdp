package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class PitchLines extends HSBCluster {

	public PitchLines(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,45,100), Color.white);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 640, 50, 480);
	}
	
	//This method is awful I know. Will clean up shortly.
	
	public List<VecI> getCorners() {
		
		 ArrayList<VecI> result = new ArrayList<VecI>();
		
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 result.add(topCorner);
		 
		 return result;
		 
	}
		 /*
		 
		 leftmostPixel = super.getMinXPixel();	 
		 xmin = leftmostPixel.x - 10;
		 xmax = leftmostPixel.x + 10;
		 yposition = leftmostPixel.y;
		 bottomCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 VecI leftmostPixel = super.getMinXPixel();	 
		 int xmin = leftmostPixel.x - 10;
		 int xmax = leftmostPixel.x + 10;
		 int yposition = leftmostPixel.y;
		 VecI topCorner = new VecI(0,0);
		 
		 yposition++;
		 
		 while(true) {
			 VecI pixel = super.getMinXPixel(yposition);
			 if(pixel.x < xmin || pixel.x > xmax) {
				 topCorner = super.getMinXPixel(yposition + 5);
				 break;
			 }
			 yposition--;
			 if(yposition<0)
				 break;
		 }
		 
		 return result;
		
	}
	
	*/
}
