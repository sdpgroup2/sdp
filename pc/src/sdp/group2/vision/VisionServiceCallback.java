package sdp.group2.vision;

import sdp.group2.geometry.Rect;
import sdp.group2.vision.clusters.BallCluster;
import sdp.group2.vision.clusters.RobotBaseCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public boolean onPreparationReady(Image currentImage, BallCluster ballCluster, RobotBaseCluster robotBaseCluster, Rect pitchRect, Rect[] sectionRects);

    public void onFrameGrabbed(Image currentImage);

    public void onImageFiltered(Image currentImage);

    public void onPreparationFrame();

	public void onImageProcessed(Image currentImage, BallCluster ballCluster, RobotBaseCluster robotBaseCluster);

	public void onExceptionThrown(Exception e);

}
