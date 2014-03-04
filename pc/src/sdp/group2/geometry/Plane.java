/** @author Jaroslaw Hirniak */

package sdp.group2.geometry;

import java.awt.Polygon;

import lejos.geom.Rectangle;

public class Plane extends Polygon {
	
	private String id = null;
	private Rectangle boundary = null;
	private int SIGNIFICANT_BOUNCSES = 10;

	public Plane(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void addPoint(Point p) {
		super.addPoint((int) p.x, (int) p.y);
	}
	
	public void setOutline(PointSet outline) {
		super.reset();
		for (Point p : outline.getPoints()) {
			addPoint(p);
		}
	}

	public boolean isWellFormed() {
		return super.npoints > 2;
	}

	public PointSet getTrajectory(Point origin, double direction) {
		PointSet trajectory = new PointSet(false);
		trajectory.add(origin);

		for (int i = 0; i < SIGNIFICANT_BOUNCSES; i++) {
			BounceConclusion bounce = getBounceConclusion(origin, direction);
			if (bounce.alpha == 100) {
				continue;
			}
			trajectory.add(bounce.intersection);
			direction = updateDirection(direction, bounce.alpha, bounce.top);
		}

		return trajectory;
	}

	private double updateDirection(double direction, double alpha, boolean top) {
		int sign = getSign(direction, top);
		direction += Math.PI + sign * 2 * alpha;

		while (direction > Math.PI) {
			direction -= 2 * Math.PI;
		}

		while (direction < -Math.PI) {
			direction += 2 * Math.PI;
		}

		return direction;
	}

	/**
	 * @param top
	 *            - is bounce wall above the Point to which direction is
	 *            attached?
	 * @return sign used in alpha refinement
	 */
	private int getSign(double direction, boolean top) {
		int section = (int) (direction / Math.PI * 2);
		boolean isEven = section % 2 == 0;
		int sign = isEven ? -1 : 1;
		return top ? sign : sign * (-1);
	}

	public boolean intersects(Line m, Line n) {
		return m.intersectsLine(n);
	}

	/** Check if the line intersects the zone */
	public boolean isInterestedByLine(Point p0, Point p1) {
		Line line = new Line(p0.x, p0.y, p1.x, p0.y);
		return isIntersectedBy(line);
	}

	public boolean isIntersectedBy(Line m) {
		for (int i = 1; i < super.npoints; i++) {
			Point p0 = new Point(super.xpoints[i - 1], super.ypoints[i - 1]);
			Point p1 = new Point(super.xpoints[i],     super.ypoints[i]    );
			Line wall = new Line(p0.x, p1.x, p0.y, p1.y);

			if (intersects(m, wall)) {
				return true;
			}
		}

		return false;
	}

	public Point getIntersection(Line line, Line wall) {
		double ix, iy;

		double s1_x, s1_y, s2_x, s2_y;
		s1_x = line.x2 - line.x1;
		s1_y = line.y2 - line.y1;
		s2_x = wall.x2 - wall.x1;
		s2_y = wall.y2 - wall.y1;

		double s, t;
		s = (-s1_y * (line.x1 - wall.x1) + s1_x * (line.y1 - wall.y1))
				/ (-s2_x * s1_y + s1_x * s2_y);
		t = (s2_x * (line.y1 - wall.y1) - s2_y * (line.x1 - wall.x1))
				/ (-s2_x * s1_y + s1_x * s2_y);

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1) { // Collision detected
			ix = line.x1 + (t * s1_x);
			iy = line.y1 + (t * s1_y);
			return new Point(ix, iy);
		}

		return null;
	}

	public Point getIntersection(Line line) {
		Point intersection = null;

		for (int i = 1; i < super.npoints; i++) {
			Point p0 = new Point(super.xpoints[i - 1], super.ypoints[i - 1]);
			Point p1 = new Point(super.xpoints[i], super.ypoints[i]);

			Line wall = new Line(p0.x, p1.x, p0.y, p1.y);

			intersection = getIntersection(line, wall);
			if (intersection != null) {
				return intersection;
			}
		}

		return intersection;
	}

	public BounceConclusion getBounceConclusion(Point origin, double direction) {
		Line line = expand(origin, direction);
		Point intersection = null;

		for (int i = 1; i < super.npoints; i++) {
			Point p0 = new Point(super.xpoints[i - 1], super.ypoints[i - 1]);
			Point p1 = new Point(super.xpoints[i], super.ypoints[i]);

			Line wall = new Line(p0.x, p1.x, p0.y, p1.y);

			intersection = getIntersection(line, wall);
			if (intersection != null) {
				boolean top = intersection.x < origin.y;
				Point endpoint = (Point) (-Math.PI / 2 < direction
						&& direction < Math.PI / 2 ? wall.getP1() : wall
						.getP2());
				double alpha = Math.atan2(origin.y - endpoint.y, origin.x
						- endpoint.x);
				return new BounceConclusion(top, alpha, intersection);
			}
		}

		// throw new
		// IllegalStateException("Expected closed polygon and inside point, but did not find.");
		return new BounceConclusion(false, 100.0, new Point(0.0, 0.0));
	}

	private class BounceConclusion {
		public boolean top;
		public double alpha;
		public Point intersection;

		public BounceConclusion(boolean top, double alpha, Point intersection) {
			this.top = top;
			this.alpha = alpha;
			this.intersection = intersection;
		}
	}

	public Rectangle getBoundary() {
		if (boundary == null) {
			double minX = Float.MAX_VALUE;
			double minY = Float.MAX_VALUE;
			double maxX = Float.MIN_VALUE;
			double maxY = Float.MIN_VALUE;

			for (int i = 0; i < super.npoints; i++) {
				minX = Math.min(minX, super.xpoints[i]);
				minY = Math.min(minY, super.ypoints[i]);
				maxX = Math.max(maxX, super.xpoints[i]);
				maxY = Math.max(maxY, super.ypoints[i]);
			}

			boundary = new Rectangle((float) minX, (float) minY,
					(float) (maxX - minX), (float) (maxY - minY));
		}

		return boundary;
	}

	public Line expand(Point origin, double direction) {
		boundary = getBoundary();
		double width = boundary.getWidth();
		double height = boundary.getHeight();
		Line line = new Line(origin.x, origin.y, origin.x + width
				* Math.tan(direction), origin.y + height * Math.tan(direction));

		return line;
	}

	public Line expand(Point p0, Point p1) {
		return expand(p0, Math.atan2(p1.y - p0.y, p1.x - p0.x));
	}

	public Line expand(Line line) {
		return expand(new Point(line.x1, line.y1), new Point(line.x2, line.y2));
	}

	public static double pix2mm(int pix) {
		/*
		 * pitch dimension in pixels in room 3.11 2165 / 552 -> 3.922101449 1140
		 * / 302 high -> 3.774834437 average -> 3.848467943
		 */
		return (double) ((int) (pix * 3.848467943 + .5)); // round around the
															// middle
	}

	public static int rad2deg(double rad) {
		return (int) (180.0 * rad / Math.PI);
	}
	
	public PointSet getOutline() {
		PointSet outline = new PointSet();
		
		for (int i = 0; i < super.npoints; i++) {
			outline.add(new Point(xpoints[i], ypoints[i]));
		}
		
		return outline;
	}
	
	public boolean contains(Point p) {
//		System.out.println("---");
//		System.out.println(p);
//		
//		System.out.println("---");
		return super.contains(p.x / 0.259843661, p.y / 0.259843661);
	}

}
