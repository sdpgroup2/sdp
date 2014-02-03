package group2.sdp.pc.vision;

import java.awt.Color;

public class HSBColor {
	
	public static final int REDMASK = new Color(255,0,0,0).getRGB();
	public static final int GREENMASK = new Color(0,255,0,0).getRGB();
	public static final int BLUEMASK = new Color(0,0,255,0).getRGB();
	
	public final float h;
	public final float s;
	public final float b;
	
	
	public static boolean inRange(HSBColor color, HSBColor minColor, HSBColor maxColor) {
		return (minColor.s <= color.s && color.s <= maxColor.s &&
				minColor.b <= color.b && color.b <= maxColor.b &&
				((minColor.h <= maxColor.h) ? (minColor.h <= color.h && color.h <= maxColor.h) : (color.h >= minColor.h || color.h <= maxColor.h)));
	}
	
	public HSBColor(int color) {
		float[] hsb = Color.RGBtoHSB((color & REDMASK) >> 16, (color & GREENMASK) >> 8, (color & BLUEMASK), null);
		h = hsb[0];
		s = hsb[1];
		b = hsb[2];
	}
	
	public HSBColor(float h, float s, float b) {
		this.h = h;
		this.s = s;
		this.b = b;
	}
	
	/**
	 * This constructor uses the same format as the GIMP.
	 * @param h The hue, from 0 to 360
	 * @param s The saturation, from 0 to 100
	 * @param b The brightness, from 0 to 100
	 */
	public HSBColor(int h, int s, int b) {
		this.h = ((float) h) / 360;
		this.s = ((float) s) / 100;
		this.b = ((float) b) / 100;
	}
	
	public Color getRGBColor() {
		return Color.getHSBColor(h, s, b);
	}
	
	public int iHue() {
		return (int)(h*360);
	}
	
	public int iSaturation() {
		return (int)(s*100);
	}
	
	public int iBrightness() {
		return (int)(b*100);
	}
}
