package group2.sdp.pc.vision;

import java.awt.image.BufferedImage;

public interface VisionServiceCallback {

    public void onFrameGrabbed(BufferedImage image);

    public void onImageFiltered(HSBColor[] hsbArray);

    public void onPreparationFrame();

	public void onImageProcessed();

}
