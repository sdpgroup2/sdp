package group2.sdp.pc.util;

import group2.sdp.pc.geom.Line;
import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.Vector;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Debug {

	public static boolean DEBUG_LOGGING = true;
	public static boolean VISION_FILL_PIXELS = false;
	public static boolean VISION_DRAW_BOUNDS = false;
	public static boolean VISION_NORMALIZE_IMAGE = false;

	public static void log(String message) {
		if (DEBUG_LOGGING) {
			System.out.println(message);
		}
	}

	public static void logf(String message, Object... args) {
		if (DEBUG_LOGGING) {
			System.out.printf(message+"\n", args);
		}
	}

	public static void drawPixel(BufferedImage dest, int x, int y, Color color) {
		if (VISION_FILL_PIXELS) {
			dest.setRGB(x, y, color.getRGB());
		}
	}

	public static void drawTestPixel(BufferedImage dest, int x, int y, Color color) {
			dest.setRGB(x, y, color.getRGB());

	}

	public static void drawRect(BufferedImage dest, Rect rect, Color unused) {
		if (VISION_DRAW_BOUNDS && rect != null) {
			Graphics g = dest.getGraphics();
			g.setColor(Color.WHITE);
			g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
		}
	}

	public static void drawLine(BufferedImage dest, Line line) {
		Graphics g = dest.getGraphics();
		g.setColor(Color.magenta);
		g.drawLine((int) line.x1, (int) line.x2, (int) line.y1, (int) line.y2);
	}
	
	public static void drawVector(BufferedImage dest, Point pos, Vector vec) {
		if (VISION_DRAW_BOUNDS && pos != null && vec != null) {
			Graphics g = dest.getGraphics();
			g.setColor(Color.magenta);
			g.drawOval((int) pos.x -2 , (int) pos.y - 2, 4, 4);
			g.drawLine((int) pos.x, (int) pos.y, (int) (pos.x + vec.x), (int) (pos.y + vec.y));
		}
	}

}
