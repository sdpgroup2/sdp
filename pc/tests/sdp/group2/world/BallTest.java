package sdp.group2.world;

import sdp.group2.geometry.Point;
import junit.framework.TestCase;


public class BallTest extends TestCase {

	Ball b;
	
	protected void setUp() throws Exception {
		b = new Ball(new Point (1.0, 1.0));
	}
	
	public void testGetRadius() {
		assertEquals(25, b.getRadius());
	}
	
	public void testBallPosition() {
		assertEquals(new Point(1.0, 1.0), b.getPosition());
	}

}
