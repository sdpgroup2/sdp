package group2.sdp.pc.util;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.world.Pitch;

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
