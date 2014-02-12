package group2.sdp.pc;

import group2.sdp.pc.ai.DefensivePlanner;
import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;
import group2.sdp.pc.util.Constants.PitchType;
import group2.sdp.pc.util.Constants.TeamColor;
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

import java.awt.image.BufferedImage;
import java.util.List;

public class Milestone3 implements VisionServiceCallback {
	
	public static TeamColor ourTeam;
	public static PitchType pitchPlayed;
	private Pitch pitch;
	private VisionService visionService;
	private Sender sender = null;
	private Vector robotDirectionVector;
	private DefensivePlanner defPlanner;
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
	
	public Milestone3() {
		this.visionService = new VisionService(5, this);
		this.visionService.start();
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
	public boolean onPreparationReady(HSBColor[] hsbArray,
			BallCluster ballCluster, RobotBaseCluster robotBaseCluster,
			Rect pitchRect, Rect[] sectionRects) {
		
		this.pitch = new Pitch(pitchRect, sectionRects);
		System.out.println(sectionRects[0]);
		this.defPlanner = new DefensivePlanner(pitch, (byte) 0);
		
		List<Rect> ballImpRects = ballCluster.getImportantRects();
		if (ballImpRects == null || ballImpRects.size() == 0) {
			// If we don't find the ball then loop until we do
			System.out.println("No ball found in preparation.");
			return false;
		}
		Ball ball = new Ball(ballImpRects.get(0));
		pitch.addBall(ball);
		
		List<Rect> robotImpRects = robotBaseCluster.getImportantRects();
		if (robotImpRects == null || robotImpRects.size() == 0) {
			// If we don't find the robot then loop until we do
			System.out.println("No robot found in preparation.");
			return false;
		}
		Rect blueRobotRect = robotImpRects.get(0);
		Vector blueRobotDirection = robotBaseCluster.getRobotVector(hsbArray);
		pitch.addRobot(new Robot(blueRobotRect, blueRobotDirection));
		return true;
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
			return;
		}
		Point blueRobotPosition = robotRects.get(0).getCenter();
		
		// Update the robot direction
		Vector blueRobotDirection = robotBaseCluster.getRobotVector(hsbArray);
		if (blueRobotDirection == null) {
			System.out.println("No direction for the robot.");
			return;
		}
		
		// Average out the direction vector
		if (robotDirectionVector == null) {
			robotDirectionVector = blueRobotDirection;
		} else if (blueRobotDirection != null) {
			robotDirectionVector.averageWith(blueRobotDirection);
		}
		
		pitch.updateRobotState(blueRobotPosition, robotDirectionVector);
		
		System.out.println("Acted!");
		
		this.defPlanner.act();
		
//		pitch.updateRobotState(blueRobotPosition, blueRobotDirection);
		
		// Calculate the vector between ball and robot
		// Vector vectorToGo = pitch.getRobotBallVector();
		
		// Calculate the angle we need to turn
		// double angleToTurn = pitch.getRobot().angleToVector(vectorToGo);
		// System.out.println(angleToTurn);
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

	@Override
	public void onExceptionThrown(Exception e) {
		this.defPlanner.disconnect();
	}
	
}
