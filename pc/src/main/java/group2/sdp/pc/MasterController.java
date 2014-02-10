package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.VisionService;
import group2.sdp.pc.vision.VisionServiceCallback;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;
import group2.sdp.pc.world.Ball;
import group2.sdp.pc.world.Pitch;
import group2.sdp.pc.world.Robot;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MasterController implements VisionServiceCallback {

	private Pitch pitch;
	private VisionService visionService;

	public static void main(String[] args) {
		MasterController mc = new MasterController();
	}

	@Override
	public void onImageProcessed() {
	}

	@Override
	public void onPreparationReady(PitchLines lines, PitchSection sections,
			BallCluster ballCluster, YellowRobotCluster yellowCluster, BlueRobotCluster blueCluster) {
		this.pitch = new Pitch(lines, sections);
		Ball ball = new Ball(ballCluster.getImportantRects().get(0));
		pitch.addBall(ball);
		List<Robot> blueRobots = new ArrayList<Robot>();
		List<Robot> yellowRobots = new ArrayList<Robot>();
		for (Rect rect : blueCluster.getImportantRects().subList(0, 2)) {
			blueRobots.add(new Robot(rect));
		}
		for (Rect rect : yellowCluster.getImportantRects().subList(0, 2)) {
			yellowRobots.add(new Robot(rect));
		}
		pitch.addRobots(blueRobots, yellowRobots);
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

}
