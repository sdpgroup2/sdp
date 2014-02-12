package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.RobotBaseCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public boolean onPreparationReady(HSBColor[] hsbArray, BallCluster ballCluster, RobotBaseCluster robotBaseCluster, Rect pitchRect, Rect[] sectionRects);

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray, BallCluster ballCluster, RobotBaseCluster robotBaseCluster);

	public void onExceptionThrown(Exception e);
	
}
