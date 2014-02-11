package group2.sdp.pc.vision;

import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.PitchLinesCluster;
import group2.sdp.pc.vision.clusters.PitchSectionCluster;
import group2.sdp.pc.vision.clusters.RobotBaseCluster;
import group2.sdp.pc.vision.clusters.RobotCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public void onPreparationReady(HSBColor[] hsbArray, PitchLinesCluster lines, PitchSectionCluster sections, BallCluster ballCluster, RobotBaseCluster robotBaseCluster);

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray, BallCluster ballCluster, RobotBaseCluster robotBaseCluster);

}
