package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.ColorConfig;

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
		return getRects(4, 24, 4, 24, 0.5f, 1.1f);
	}

}
