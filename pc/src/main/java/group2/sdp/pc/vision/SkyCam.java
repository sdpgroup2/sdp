package group2.sdp.pc.vision;

import group2.sdp.pc.Debug;

import java.awt.Dimension;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


public class SkyCam {
	
	public static final String defaultDevice = "/dev/video0";
	public static final String requiredInputName = "S-Video";
	public static final int requiredStandard = V4L4JConstants.STANDARD_PAL;
	
	private VideoDevice device;
	private DeviceInfo deviceInfo;
	private InputInfo inputInfo = null;
	private JPEGFrameGrabber frameGrabber;
	
	
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
			frameGrabber = device.getJPEGFrameGrabber(
					640, 480, inputInfo.getIndex(), requiredStandard, V4L4JConstants.MAX_JPEG_QUALITY);
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}
	
	public SkyCam() {
		this(defaultDevice);
	}
	
	public void startVision(CaptureCallback callback) {
		Debug.log("Vision started.");
		try {
			frameGrabber.setCaptureCallback(callback);
			frameGrabber.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}
	
	public void stopVision() {
		Debug.log("Vision stopped.");
		frameGrabber.stopCapture();
	}
	
	public void release() {
		Debug.log("Camera device released.");
		device.releaseFrameGrabber();
		device.release();
	}
	
	public Dimension getSize() {
		return new Dimension(frameGrabber.getWidth(), frameGrabber.getHeight());
	}
	
}
