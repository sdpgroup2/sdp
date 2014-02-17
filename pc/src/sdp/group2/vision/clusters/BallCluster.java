package sdp.group2.vision.clusters;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.ColorConfig;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for all pixels that could be part of the ball.
 * @author Paul Harris
 */
public class BallCluster extends HSBCluster {
	
	public BallCluster(String name) {
		super(name, ColorConfig.BALL_2_MIN, ColorConfig.BALL_2_MAX, Color.red);
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(8, 24, 8, 24, 0.5f, 1.1f);
	}

}
