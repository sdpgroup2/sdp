package group2.sdp.pc.vision;

import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.PitchLinesCluster;
import group2.sdp.pc.vision.clusters.PitchSectionCluster;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public void onPreparationReady(PitchLinesCluster lines, PitchSectionCluster sections, BallCluster ballCluster,
			YellowRobotCluster yellowCluster, BlueRobotCluster blueCluster);

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray);

}
