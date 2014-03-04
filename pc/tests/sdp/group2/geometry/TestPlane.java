package sdp.group2.geometry;

import static org.junit.Assert.*;

import org.junit.Test;


public class TestPlane {

	@Test
	public void testContains() {
		PointSet ps0 = new PointSet();
		ps0.add(0, 0);
		ps0.add(10, 0);
		ps0.add(10, 10);
		ps0.add(0, 10);
		
		Point pointIn = new Point(5, 5);
		Point pointOut = new Point(15, 15);
		
		assertTrue(ps0.contains(pointIn));
		assertFalse(ps0.contains(pointOut));
	}

}
