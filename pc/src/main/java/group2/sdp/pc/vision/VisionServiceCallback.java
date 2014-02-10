package group2.sdp.pc.vision;

import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public void onPreparationReady(PitchLines lines, PitchSection sections, BallCluster ballCluster,
			YellowRobotCluster yellowCluster, BlueRobotCluster blueCluster);

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed();

}
