package group2.sdp.pc.vision;

import group2.sdp.pc.vision.clusters.HSBCluster;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

	public void onPreparationReady(BufferedImage image, HSBCluster[] clusters);

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed(BufferedImage image, HSBColor[] hsbArray);

}
