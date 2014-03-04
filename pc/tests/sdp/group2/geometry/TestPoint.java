package sdp.group2.geometry;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestPoint {

	private static double eps = 1e-9;
	
	@Test
	public void testGetAngle() {
		// Right angle
		Point p0 = new Point(10, 0);
		Point p1 = new Point(0, 10);
		assertEquals(p0.getAngle(p1), Math.PI / 2);
		
		// 45 deg
		Point p2 = new Point(10, 0);
		Point p3 = new Point(10, 10);
		assertEquals(p2.getAngle(p3), Math.PI / 4);
	}
	
	public static void assertEquals(double p, double q) {
		assertTrue(Math.abs(p - q) < eps);
	}

}
