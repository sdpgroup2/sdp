package sdp.group2.vision;

import sdp.group2.geometry.MathU;

import java.awt.Color;

public class HSBColor {
	
	public static final int REDMASK = new Color(255,0,0,0).getRGB();
	public static final int GREENMASK = new Color(0,255,0,0).getRGB();
	public static final int BLUEMASK = new Color(0,0,255,0).getRGB();
	public static boolean normalizeRGB = false;
	
	public float h;
	public float s;
	public float b;
	float[] innerArray = new float[3];
	
	
	public static boolean inRange(HSBColor color, HSBColor minColor, HSBColor maxColor) {
		return (minColor.s <= color.s && color.s <= maxColor.s &&
				minColor.b <= color.b && color.b <= maxColor.b &&
				((minColor.h <= maxColor.h) ? (minColor.h <= color.h && color.h <= maxColor.h) : (color.h >= minColor.h || color.h <= maxColor.h)));
	}
	
	public HSBColor() {
		set(0,0,0);
	}
	
	public HSBColor(int color) {
		set(color);
	}
	
	public HSBColor(float h, float s, float b) {
		set(h,s,b);
	}
	
	public HSBColor(int h, int s, int b) {
		set(h,s,b);
	}
	
	public HSBColor(Color color) {
		set(color);
	}
	
	public HSBColor set(float h, float s, float b) {
		this.h = h;
		this.s = s;
		this.b = b;
		return this;
	}
	
	public HSBColor set(int rgbColor) {
//		rgbColor = normalizeRGB(rgbColor);
		Color.RGBtoHSB((rgbColor & REDMASK) >> 16, (rgbColor & GREENMASK) >> 8, (rgbColor & BLUEMASK), innerArray);
		this.h = innerArray[0];
		this.s = innerArray[1];
		this.b = innerArray[2];
		
		return this;
	}
	
	public HSBColor set(int h, int s, int b) {
		this.h = ((float) h) / 360;
		this.s = ((float) s) / 100;
		this.b = ((float) b) / 100;
		return this;
	}
	
	public HSBColor set(Color color) {
		set(color.getRGB());
		return this;
	}
	
	public HSBColor offset(float dh, float ds, float db) {
		this.h += dh;
		this.s = MathU.clamp(s+ds);
		this.b = MathU.clamp(b+db);
		return this;
	}
	
	public Color getRGBColor() {
		return Color.getHSBColor(h, s, b);
	}
	
	public int getRGB() {
		return Color.HSBtoRGB(h, s, b);
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
	
	private int normalizeRGB(int rgb)
	{
		if (!normalizeRGB) { return rgb; }
		
		int r = (rgb & REDMASK) >> 16;
		int g = (rgb & GREENMASK) >> 8;
		int b = rgb & BLUEMASK;
		int sum = r + g + b;
		r = (int) ((double) r / sum * 255.0);
		g = (int) ((double) g / sum * 255.0);
		b = (int) ((double) b / sum * 255.0);
		r <<= 16;
		g <<= 8;
		return r + g + b;
	}
}
