package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Shape;
import sdp.group2.geometry.VecI;
import sdp.group2.vision.ColorConfig;
import sdp.group2.vision.HSBColor;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class PitchLinesCluster extends HSBCluster {
	
//	enum Dir {
//		Left,
//		Right,
//		Down,
//		Up;
//	}

	public PitchLinesCluster(String name) {
		super(name, ColorConfig.LINES_2_MIN, ColorConfig.LINES_2_MAX, Color.white);
	}
	
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(400, 640, 50, 480);
	}
	
	public Shape getPitchShape() {		
		ArrayList<VecI> corners = new ArrayList<VecI>();
		
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
		
		 VecI finalCorner = new VecI(0,0);
		 VecI edgePixel = getEdgePixel(edge); 
		 int min, max, position = 0;

		 // set a margin of error of 10 pixels perpendicular to the edge
		 // in either direction when looking for the corner. 
	 
		 if (edge == Dir.Left || edge == Dir.Right) {
			 min = edgePixel.x - 10;
			 max = edgePixel.x + 10;
			 position = edgePixel.y; 
		 }
		 else {
			 min = edgePixel.y - 10;
			 max = edgePixel.y + 10;
			 position = edgePixel.x;                                        
		 }

		 if (edge == Dir.Right || edge == Dir.Down)
			 position++;
		 else
		 	 position--;				

		 boolean edgeFound = false;
		 
		 while (!edgeFound) {
			 VecI pixel = getEdgePixel(position, edge);
			 			 
			 // We check which corner we are looking for (8 possible cases), and if
			 // the current pixel is outside the 20 pixel margin of error, we backtrack
			 // by 5 pixels and set this as the corner we are looking for (backtracking is
			 // to make up for the overshot lookahead of the loop).
			 // If the pixel is within the margin, we simply move to the next pixel.
			 
			 switch (edge) {
			 case Left:
			 case Right: {
				 if (position <= 0 || position >= 480)
					 return null;
				 switch (corner) {
				 case Up: {					
					 if(pixel.x < min || pixel.x > max) {
						 finalCorner = getEdgePixel(position + 5, edge);
						 edgeFound = true;
						 break;
					 }
					 position--;
					 break;
				 }
				 case Down: {					 						 
					 if(pixel.x < min || pixel.x > max) {
						 finalCorner = getEdgePixel(position - 5, edge);
						 edgeFound = true;
						 break;
					 }
					 position++;
					 break;
				 }
				 }
				 break;
			 }
			 case Up:
			 case Down: {
				 if (position <= 0 || position >= 480)
					 return null;
				 switch (corner) {
				 case Left: {
					 if(pixel.y < min || pixel.y > max) {
						 finalCorner = getEdgePixel(position + 5, edge);
						 edgeFound = true;
						 break;
					 }
					 position--;
					 break;
				 }
				 case Right: {
					 if(pixel.y < min || pixel.y > max) {
						 finalCorner = getEdgePixel(position - 5, edge);
						 edgeFound = true;
						 break;
					 }
					 position++;
					 break;
				 }
				 }
				 break;
			 }
			 }
		 }
		 
		 return finalCorner;
	}
		 
}