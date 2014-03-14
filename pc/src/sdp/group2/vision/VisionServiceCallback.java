package sdp.group2.vision;

import java.awt.image.BufferedImage;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.util.Tuple;


public interface VisionServiceCallback {

	public void onExceptionThrown(Exception e);
	
	public void update(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots);

	void prepared(Point ballCentroid, List<Tuple<Point, Point>> yellowRobots,
			List<Tuple<Point, Point>> blueRobots);
	
	void getImage(BufferedImage image);

}
