package sdp.group2.util;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.world.Pitch;

public class CoordinateTranslator {

	// Used for converting from pixels to mms
	public static final double SCALE = 1;

	// did not working because [getPitchRect()] method was commented, so I commented the below out to be able to compile
//	public static Point fromVisionToPitch(Point pt, Pitch pitch) {
//		Rect pitchRect = pitch.getPitchRect();
//		Point topLeft = new Point(pitchRect.getX(), pitchRect.getY());
//		Vector transVector = pt.sub(topLeft);
//		transVector.scale(SCALE);
//		return pt.sub(transVector);
//	}

}
