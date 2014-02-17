//package sdp.group2.pc;
//
//import sdp.group2.geometry.Rect;
//import sdp.group2.vision.HSBColor;
//import sdp.group2.vision.VisionService;
//import sdp.group2.vision.VisionServiceCallback;
//import sdp.group2.vision.clusters.BallCluster;
//import sdp.group2.vision.clusters.BlueRobotCluster;
//import sdp.group2.vision.clusters.PitchLinesCluster;
//import sdp.group2.vision.clusters.PitchSectionCluster;
//import sdp.group2.vision.clusters.YellowRobotCluster;
//import sdp.group2.world.Ball;
//import sdp.group2.world.Constants;
//import sdp.group2.world.Constants.PitchType;
//import sdp.group2.world.Constants.TeamColor;
//import sdp.group2.world.Pitch;
//import sdp.group2.world.Robot;
//
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MasterController implements VisionServiceCallback {
//
//	public static TeamColor ourTeam;
//	public static PitchType pitchPlayed;
//	private Pitch pitch;
//	private VisionService visionService;
//
//	public static void main(String[] args) {
//		if (args.length < 2) {
//			System.out.println("Not specified which team we are and what pitch we're playing");
//			System.exit(0);
//		}
//		ourTeam = Integer.parseInt(args[0]) == Constants.YELLOW_TEAM ? TeamColor.YELLOW : TeamColor.BLUE;
//		pitchPlayed = Integer.parseInt(args[1]) == Constants.MAIN_PITCH ? PitchType.MAIN : PitchType.SIDE;
//		MasterController mc = new MasterController();
//	}
//
//	@Override
//	public void onPreparationReady(PitchLinesCluster lines, PitchSectionCluster sections,
//			BallCluster ballCluster, YellowRobotCluster yellowCluster, BlueRobotCluster blueCluster) {
//		this.pitch = new Pitch(lines, sections);
//		Ball ball = new Ball(ballCluster.getImportantRects().get(0));
//		pitch.addBall(ball);
//		List<Robot> blueRobots = new ArrayList<Robot>();
//		List<Robot> yellowRobots = new ArrayList<Robot>();
//		for (Rect rect : blueCluster.getImportantRects().subList(0, 2)) {
//			blueRobots.add(new Robot(rect));
//		}
//		for (Rect rect : yellowCluster.getImportantRects().subList(0, 2)) {
//			yellowRobots.add(new Robot(rect));
//		}
//		pitch.addRobots(blueRobots, yellowRobots, ourTeam);
//	}
//
//	@Override
//	public void onFrameGrabbed(BufferedImage image) {
//	}
//
//	@Override
//	public void onImageFiltered(HSBColor[] hsbArray) {
//	}
//
//	@Override
//	public void onPreparationFrame() {};
//
//	public MasterController() {
//		this.visionService = new VisionService(5, this);
//	}
//
//	@Override
//	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
