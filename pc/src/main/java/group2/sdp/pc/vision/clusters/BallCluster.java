package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;

import java.util.List;


/**
 * A cluster for all pixels that could be part of the ball.
 * @author Paul Harris
 */
public class BallCluster extends HSBCluster {
	
	public BallCluster(String name) {
		super(name, new HSBColor(348, 70, 40), new HSBColor(20, 90, 90));
	}
	
	public Rect getBallRect() {
		List<Rect> rects = getRects(8, 24, 8, 24);
		if (rects.isEmpty()) {
			return null;
		}
		return rects.get(0);
	}

}
