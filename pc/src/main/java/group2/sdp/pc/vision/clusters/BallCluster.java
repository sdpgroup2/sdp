package group2.sdp.pc.vision.clusters;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.util.ColorConfig;

import java.awt.Color;
import java.util.List;


/**
 * A cluster for all pixels that could be part of the ball.
 * @author Paul Harris
 */
public class BallCluster extends HSBCluster {
	
	public BallCluster(String name) {
<<<<<<< HEAD
		super(name, ColorConfig.BALL_2_MIN, ColorConfig.BALL_2_MAX, Color.red);
=======
		super(name, new HSBColor(343, 85, 50), new HSBColor(30, 100, 100), Color.red);
>>>>>>> milestone3
	}
	
	@Override
	public List<Rect> getImportantRects() {
		return getRects(4, 24, 4, 24, 0.5f, 1.1f);
	}

}
