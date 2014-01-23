package group2.sdp.pc.vision;

import group2.sdp.pc.Log;

import java.awt.Color;
import java.awt.image.BufferedImage;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


public class SkyCam {
	
	public static final String defaultDevice = "/dev/video0";
	public static final String requiredInputName = "S-Video";
	public static final int requiredStandard = V4L4JConstants.STANDARD_PAL;
	
	private VideoDevice device;
	private DeviceInfo deviceInfo;
	private InputInfo inputInfo = null;
	
	
	public SkyCam(String deviceName) {
		try {
			device = new VideoDevice(deviceName);
			deviceInfo = device.getDeviceInfo();
			for (InputInfo i: deviceInfo.getInputs()) {
				if (i.getName().equals(requiredInputName)) {
					inputInfo = i;
					break;
				}
			}
			if (inputInfo == null) {
				throw new RuntimeException("Video device has no "+requiredInputName+" input mode.");
			}
		} catch (V4L4JException e) {
			throw new RuntimeException("Failed to access device '"+deviceName+"'");
		}
	}
	
	public SkyCam() {
		this(defaultDevice);
	}
	
	public void release() {
		device.release();
	}
	
	public void GrabJPEG() {
		if (inputInfo.getSupportedStandards().contains(requiredStandard)) {
			try {
				final JPEGFrameGrabber grabber = device.getJPEGFrameGrabber(
						640, 480, inputInfo.getIndex(), requiredStandard, V4L4JConstants.MAX_JPEG_QUALITY);
				
				CaptureCallback capturer = new CaptureCallback() {
					public void nextFrame(VideoFrame frame) {
						Log.print("Captured frame");
						BufferedImage image = frame.getBufferedImage();
						float r = 0;
						float g = 0;
						float b = 0;
						for (int x=0; x<image.getWidth(); x++) {
							for (int y=0; y<image.getHeight(); y++) {
								Color col = new Color(image.getRGB(x, y));
								r += col.getRed();
								g += col.getGreen();
								b += col.getBlue();
							}
						}
						int pixels = image.getHeight() * image.getWidth();
						Log.print("Average colour is: (" + r/pixels +", " + (g/pixels) + ", " + (b/pixels) + ")");
						frame.recycle();
						stopGrab(grabber);
					}
					public void exceptionReceived(V4L4JException e) {
						Log.print("Capture failed.");
					}
				};
				
				grabber.setCaptureCallback(capturer);
				grabber.startCapture();
			} catch (V4L4JException e) {
			
			}
		}
	}
	
	private void stopGrab(JPEGFrameGrabber grabber) {
		grabber.stopCapture();
		device.releaseFrameGrabber();
	}
	
}
