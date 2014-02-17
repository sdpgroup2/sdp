package group2.sdp.pc.vision.filter;

import java.awt.Color;

public class ImageStatus {
	
	public static final int REDMASK = new Color(255,0,0,0).getRGB();
	public static final int GREENMASK = new Color(0,255,0,0).getRGB();
	public static final int BLUEMASK = new Color(0,0,255,0).getRGB();

	public final int meanRed;
	public final int meanGreen;
	public final int meanBlue;
	
	
	public ImageStatus(int[] rgbArray) {
		int totalRed = 0;
		int totalGreen = 0;
		int totalBlue = 0;
		for (int i=0; i<rgbArray.length; i++) {
			totalRed += (rgbArray[i] & ImageStatus.REDMASK) >> 16;
			totalGreen += (rgbArray[i] & ImageStatus.GREENMASK) >> 8;
			totalBlue += (rgbArray[i] & ImageStatus.BLUEMASK);
		}
		meanRed = totalRed / rgbArray.length;
		meanGreen = totalGreen / rgbArray.length;
		meanBlue = totalBlue / rgbArray.length;
	}
	
	@Override
	public String toString() {
		return String.format("ImageStatus[ meanRGB=(%d,%d,%d) ]", meanRed, meanGreen, meanBlue);
	}
	
}
