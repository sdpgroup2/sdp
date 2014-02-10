package group2.sdp.pc;

import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.VisionService;
import group2.sdp.pc.vision.VisionServiceCallback;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.PitchLinesCluster;
import group2.sdp.pc.vision.clusters.PitchSectionCluster;
import group2.sdp.pc.vision.clusters.RobotBaseCluster;
import group2.sdp.pc.world.Ball;
import group2.sdp.pc.world.Constants;
import group2.sdp.pc.world.Constants.PitchType;
import group2.sdp.pc.world.Constants.TeamColor;
import group2.sdp.pc.world.Pitch;
import group2.sdp.pc.world.Robot;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Milestone3 implements VisionServiceCallback {
	
	public static TeamColor ourTeam;
	public static PitchType pitchPlayed;
	private Pitch pitch;
	private VisionService visionService;
	private Sender sender;
	
	public static void main(String[] args) {
//		if (args.length < 2) {
//			System.out.println("Not specified which team we are and what pitch we're playing");
//			System.exit(0);
//		}
//		ourTeam = Integer.parseInt(args[0]) == Constants.YELLOW_TEAM ? TeamColor.YELLOW : TeamColor.BLUE;
//		pitchPlayed = Integer.parseInt(args[1]) == Constants.MAIN_PITCH ? PitchType.MAIN : PitchType.SIDE;
		new Milestone3();
	}
	
	@Override
	public void onPreparationReady(HSBColor[] hsbArray, PitchLinesCluster lines,
			PitchSectionCluster sections, BallCluster ballCluster,
			RobotBaseCluster robotCluster) {
		this.pitch = new Pitch(lines, sections);
		Ball ball = new Ball(ballCluster.getImportantRects().get(0));
		pitch.addBall(ball);
		Rect blueRobotRect = robotCluster.getImportantRects().get(0);
		Vector blueRobotDirection = robotCluster.getRobotVector(hsbArray);
		pitch.addRobot(new Robot(blueRobotRect, blueRobotDirection));
	}

	@Override
	public void onFrameGrabbed(BufferedImage image) {
	}

	@Override
	public void onImageFiltered(HSBColor[] hsbArray) {
	}

	@Override
	public void onPreparationFrame() {
	}

	@Override
	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray,
			BallCluster ballCluster, RobotBaseCluster robotCluster) {
		
		// Update the ball position
		Point ballPosition = ballCluster.getImportantRects().get(0).getCenter();
		pitch.updateBallPosition(ballPosition);
		
		// Update the robot position
		Point blueRobotPosition = robotCluster.getImportantRects().get(0).getCenter();
		Vector blueRobotDirection = robotCluster.getRobotVector(hsbArray);
		pitch.updateRobotState(blueRobotPosition, blueRobotDirection);
		
		// Calculate the vector between ball and robot
		Vector vectorToGo = pitch.getRobotBallVector();
		
		// Calculate the angle we need to turn
		double angleToTurn = pitch.getRobot().angleToVector(vectorToGo);		
		try {
			sender.rotate((short) angleToTurn, (short) 400);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Milestone3() {
		this.visionService = new VisionService(5, this);
		try {
			sender = new Sender("SDP 2A","00165307D55F");
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done bluetooth and vision");
		
	}
	
}
