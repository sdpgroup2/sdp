package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Shape;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class PitchLines extends HSBCluster {

	public PitchLines(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,70,100), Color.white);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 640, 50, 480);
	}
	
	public Shape getPitchShape() {		
		ArrayList<VecI> corners = new ArrayList<VecI>();
		
		corners.add(getCorner("L","D"));
		corners.add(getCorner("L","U"));
		corners.add(getCorner("U","L"));
		corners.add(getCorner("U","R"));
		corners.add(getCorner("R","U"));
		corners.add(getCorner("R","D"));
		corners.add(getCorner("D","R"));
		corners.add(getCorner("D","L"));
		
		Shape finalShape = new Shape(corners);
		return finalShape;
	}
	
	/**
	 * Finds a corner pixel of the pitch lines.
	 * 
	 * 
	 * @param edge    The edge the corner lies on (L/R/D/U for Left/Down/Right/Up)
	 * @param corner  Which corner of the edge we want to get (L/R/D/U)
	 * 
	 * @return The vector corresponding to the desired corner of the pitch
	 * 
	 * Note: I would have used switch statements to make the code tidier,
	 * however this version of Java does not seem to support strings as
	 * conditions for switches.
	 */
	
	public VecI getCorner(String edge, String corner) {
		
		 VecI finalCorner, edgePixel = new VecI(0,0);
		 int min, max, position = 0;
		 
		 // Start by taking the furthest pixel on the edge the corner
		 // belongs to.
		 if(edge.equals("L"))
			 edgePixel = super.getMinXPixel();
		 else if(edge.equals("R"))
			 edgePixel = super.getMaxXPixel();
		 else if(edge.equals("U"))
			 edgePixel = super.getMinYPixel();
		 else if(edge.equals("D"))
			 edgePixel = super.getMaxYPixel();
		 
		 // set a margin of error of 10 pixels perpendicular to the edge
		 // in either direction when looking for the corner. 
		 if(edge.equals("L") || edge.equals("R")) {
			 min = edgePixel.x - 10;
			 max = edgePixel.x + 10;
			 position = edgePixel.y;   // y value of the current pixel being
			                           // looked at
		 }
		 else {
			 min = edgePixel.y - 10;
			 max = edgePixel.y + 10;
			 position = edgePixel.x;   // x value of the current pixel being
                                       // looked at
		 }			 		
		 
		 
		 // move 1 pixel towards the corner we are looking for
		 if(edge.equals("R") || edge.equals("D"))
			 position++;
		 else
			 position--;
		 
		 // In this part, we loop through the pixels along the edge of the
		 // cluster towards the corner. Each time we increment the perpendicular
		 // axis coordinate by 1 (for example the y value if we are on the left or
		 // right edges, which will move us up or down depending on the corner), and
		 // have the other coordinate correspond to the furthest away pixel along that axis.		 
		 while(true) {
			 VecI pixel = new VecI(0,0);
			 if(edge.equals("L"))
				 pixel = super.getMinXPixel(position);
			 else if(edge.equals("R"))
				 pixel = super.getMaxXPixel(position);
			 else if(edge.equals("U"))
				 pixel = super.getMinYPixel(position);
			 else if(edge.equals("D"))
				 pixel = super.getMaxYPixel(position);
			 
			 
		 // We check which corner we are looking for (8 possible cases), and if
		 // the current pixel is outside the 20 pixel margin of error, we backtrack
		 // by 5 pixels and set this as the corner we are looking for (backtracking is
		 // to make up for the overshot lookahead of the loop).
		 // If the pixel is within the margin, we simply move to the next pixel.
			 if(edge.equals("L") || edge.equals("R")) {
				 if(corner.equals("U")) {
					 if(pixel.x < min || pixel.x > max) {
						 if(edge.equals("L"))
							 finalCorner = super.getMinXPixel(position + 5);
						 else
							 finalCorner = super.getMaxXPixel(position + 5);
						 break;
					 }
					 if (position <= 0)
						 return null;
					 position--;
				 }
				 else {
					 if(pixel.x < min || pixel.x > max) {
						 if(edge.equals("L"))
						 	finalCorner = super.getMinXPixel(position - 5);
						 else
							finalCorner = super.getMaxXPixel(position - 5);
						 break;
					 }
					 if (position >= 480)
						 return null;
					 position++;
				 }
			 }
			 else {
				 if(corner.equals("L")) {
					 if(pixel.y < min || pixel.y > max) {
						 if(edge.equals("U"))
							 finalCorner = super.getMinYPixel(position + 5);
						 else
							 finalCorner = super.getMaxYPixel(position + 5);
						 break;
					 }
					 if (position <= 0)
						 return null;
					 position--;
				 }
				 else {
					 if(pixel.y < min || pixel.y > max) {
						 if(edge.equals("U"))
						 	finalCorner = super.getMinYPixel(position - 5);
						 else
							finalCorner = super.getMaxYPixel(position - 5);
						 break;
					 }
					 if (position >= 640)
						 return null;
					 position++;
				 }
			 }
		 }		
		 
		 return finalCorner;
		 
	}
		 
}
