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
import group2.sdp.pc.vision.clusters.RobotCluster;
import group2.sdp.pc.world.Ball;
import group2.sdp.pc.world.Pitch;
import group2.sdp.pc.world.Robot;
import group2.sdp.util.Constants.PitchType;
import group2.sdp.util.Constants.TeamColor;

import java.awt.image.BufferedImage;
import java.util.List;

public class Milestone3 implements VisionServiceCallback {
	
	public static TeamColor ourTeam;
	public static PitchType pitchPlayed;
	private Pitch pitch;
	private VisionService visionService;
	private Sender sender = null;
	private Vector robotDirectionVector;
//	private int robotDirectionCounter;
	
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
	public void onFrameGrabbed(BufferedImage image) {
	}

	@Override
	public void onImageFiltered(HSBColor[] hsbArray) {
	}

	@Override
	public void onPreparationFrame() {
	}

	public Milestone3() {
//		try {
//			sender = new Sender("SDP2A", "00165307D55F");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		this.visionService = new VisionService(5, this);
//		new VisionGUI(visionService);
		this.visionService.start();
	}

	@Override
	public void onPreparationReady(HSBColor[] hsbArray,
			PitchLinesCluster lines, PitchSectionCluster sections,
			BallCluster ballCluster, RobotBaseCluster robotBaseCluster) {
		this.pitch = new Pitch(lines, sections);
		Ball ball = new Ball(ballCluster.getImportantRects().get(0));
		pitch.addBall(ball);
		Rect blueRobotRect = robotBaseCluster.getImportantRects().get(0);
		Vector blueRobotDirection = robotBaseCluster.getRobotVector(hsbArray);
		pitch.addRobot(new Robot(blueRobotRect, blueRobotDirection));
	}

	@Override
	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray, BallCluster ballCluster, RobotBaseCluster robotBaseCluster) {
		// Update the ball position
		List<Rect> ballRects = ballCluster.getImportantRects();
		if (ballRects == null || ballRects.size() < 1) {
			System.out.println("No ball found.");
			return;
		}
		Point ballPosition = ballRects.get(0).getCenter();
		pitch.updateBallPosition(ballPosition);
		
		// Update the robot position
		List<Rect> robotRects = robotBaseCluster.getImportantRects();
		if (robotRects == null || robotRects.size() < 1) {
			System.out.println("No robot found.");
			System.exit(1);
		}
		Point blueRobotPosition = robotRects.get(0).getCenter();
		Vector blueRobotDirection = robotBaseCluster.getRobotVector(hsbArray);
		if (blueRobotDirection == null) {
			System.out.println("No direction for the robot.");
			System.exit(1);
		}
		
		// Average out the direction vector
		if (robotDirectionVector == null) {
			robotDirectionVector = blueRobotDirection;
		} else if (blueRobotDirection == null) {
			// Do nothing use the old vector
		} else {
			robotDirectionVector.averageWith(blueRobotDirection);
		}
		System.out.println(robotDirectionVector);
		pitch.updateRobotState(blueRobotPosition, robotDirectionVector);
//			pitch.updateRobotState(blueRobotPosition, blueRobotDirection);
		
		// Calculate the vector between ball and robot
		Vector vectorToGo = pitch.getRobotBallVector();
		
		// Calculate the angle we need to turn
		double angleToTurn = pitch.getRobot().angleToVector(vectorToGo);
		System.out.println(angleToTurn);
//			try {
//				sender.rotate((int) -angleToTurn, 40);
//				double distance = vectorToGo.length() * Constants.PX_TO_MM;
//				sender.move(1, 40, (int) distance);
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				sender.disconnect();
//			}
	}
	
}
