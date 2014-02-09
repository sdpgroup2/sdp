package group2.sdp.pc;

import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.VisionService;
import group2.sdp.pc.vision.VisionServiceCallback;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.RobotCluster;
import group2.sdp.pc.world.Pitch;
import group2.sdp.util.Debug;

import java.awt.image.BufferedImage;
import java.util.List;

public class MasterController implements VisionServiceCallback {

	private Pitch pitch;
	private VisionService visionService;

	public static void main(String[] args) {
		MasterController mc = new MasterController();
	}

	@Override
	public void onPreparationReady(BufferedImage image, HSBCluster[] clusters) {
//		this.pitch;
//		this.pitch.addBall(clusters[0].)
		RobotCluster robotCluster = (RobotCluster) clusters[2];
		List<Vector> vecs = robotCluster.getRobotVectors(this.visionService.getHSBArray());
		List<Rect> rects = robotCluster.getImportantRects();
		for (int i = 0; i < rects.size(); i++) {
			Point center = rects.get(i).getCenter();
			Line line = new Line(center.x, center.x + vecs.get(i).x, center.y, center.y + vecs.get(i).y);
			Debug.drawLine(image, line);
		}
	}

	@Override
	public void onFrameGrabbed(BufferedImage image) {
	}

	@Override
	public void onImageFiltered(HSBColor[] hsbArray) {
	}

	@Override
	public void onPreparationFrame() {};

	public MasterController() {
		this.visionService = new VisionService(5, this);
	}

	@Override
	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray) {
		// TODO Auto-generated method stub
		
	}

}
