package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Shape;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.HSBColor;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class PitchLinesCluster extends HSBCluster {
	
	enum Dir {
		Left,
		Right,
		Down,
		Up;
	}
	

	public PitchLinesCluster(String name) {
		super(name, new HSBColor(0,0,33), new HSBColor(360,21,100), Color.white);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(100, 640, 50, 480);
	}
	
	public Shape getPitchShape() {		
		ArrayList<VecI> corners = new ArrayList<VecI>();
		
		/*
		 * Hey Julien, I changed the strings here to an enum because
		 * it's a little neater. It should work just the same.
		 * 
		 * -J: Cheers, i was going to use case statements but it doesn't
		 * work with strings. Didn't know about enum types until now,
		 * but this is much better.
		 */
		
		corners.add(getCorner(Dir.Left, Dir.Down));
		corners.add(getCorner(Dir.Left, Dir.Up));
		corners.add(getCorner(Dir.Up, 	Dir.Left));
		corners.add(getCorner(Dir.Up, 	Dir.Right));
		corners.add(getCorner(Dir.Right,Dir.Up));
		corners.add(getCorner(Dir.Right,Dir.Down));
		corners.add(getCorner(Dir.Down,	Dir.Right));
		corners.add(getCorner(Dir.Down, Dir.Left));
		
		Shape finalShape = new Shape(corners);
		return finalShape;
	}
	
	/**
	 * Finds a corner pixel of the pitch lines.
	 * 
	 * @param edge    The edge the corner lies on (Left/Down/Right/Up)
	 * @param corner  Which corner of the edge we want to get (Left/Right/Down/Up)
	 * 
	 * @return The vector corresponding to the desired corner of the pitch
	 * 
	 */
	
	public VecI getCorner(Dir edge, Dir corner) {
		
		 VecI finalCorner, edgePixel = new VecI(0,0);
		 int min, max, position = 0;
		 
		 // Start by taking the furthest pixel on the edge the corner
		 // belongs to.
		 switch (edge) {
		 case Left:	edgePixel = getMinXPixel(); break;
		 case Right: edgePixel = getMaxXPixel(); break;
		 case Up: edgePixel = getMinYPixel(); break;
		 case Down: edgePixel = getMaxYPixel(); break;
		 }

		 // set a margin of error of 10 pixels perpendicular to the edge
		 // in either direction when looking for the corner. 
	 
		 if (edge == Dir.Left || edge == Dir.Right) {
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
		 if (edge == Dir.Right || edge == Dir.Down)
			 position++;
		 else
		 	 position--;
		 
		 // In this part, we loop through the pixels along the edge of the
		 // cluster towards the corner. Each time we increment the perpendicular
		 // axis coordinate by 1 (for example the y value if we are on the left or
		 // right edges, which will move us up or down depending on the corner), and
		 // have the other coordinate correspond to the furthest away pixel along that axis.		 

		 while (true) {
			 VecI pixel = new VecI(0,0);
			 switch (edge) {
			 case Left: getMinXPixel(position); break;
			 case Right: getMaxXPixel(position); break;
			 case Up: getMinYPixel(position); break;
			 case Down: getMaxYPixel(position); break;
			 }
			 			 
			 // We check which corner we are looking for (8 possible cases), and if
			 // the current pixel is outside the 20 pixel margin of error, we backtrack
			 // by 5 pixels and set this as the corner we are looking for (backtracking is
			 // to make up for the overshot lookahead of the loop).
			 // If the pixel is within the margin, we simply move to the next pixel.
			 if (edge == Dir.Left || edge == Dir.Right) {
				 if (corner == Dir.Up) {
					 if (pixel.x < min || pixel.x > max) {
						 if (edge == Dir.Left)
							 finalCorner = getMinXPixel(position + 5);
						 else
							 finalCorner = getMaxXPixel(position + 5);
						 break;
					 }
					 if (position <= 0)
						 return null;
					 position--;
				 }
				 else {
					 if(pixel.x < min || pixel.x > max) {
						 if(edge == Dir.Left)
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
				 if(corner == Dir.Left) {
					 if(pixel.y < min || pixel.y > max) {
						 if(edge == Dir.Up)
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
						 if(edge == Dir.Up)
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
