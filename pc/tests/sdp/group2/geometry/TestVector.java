package sdp.group2.geometry;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestVector {

	public static double eps = 1e-9;
	
	@Test
	public void testSignedAngleDegrees() {
		Vector east = new Vector(1, 0);
		Vector north = new Vector(0, -1);
		
		Vector northeast = new Vector(1,-1);
		Vector southwest = new Vector(-1,1);
		
		assertEquals(north.signedAngleDegrees(), 90.0);
		assertEquals(east.signedAngleDegrees(), 0.0);
		assertEquals(northeast.signedAngleDegrees(), 45.0);
		assertEquals(southwest.signedAngleDegrees(), -135.0);		
	}
	
	public static void assertEquals(double p, double q) {
		System.out.println("Testing "+p+" against "+q);
		assertTrue(""+p+" not equal to "+q, Math.abs(p - q) < eps);
	}

}
