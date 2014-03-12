package sdp.group2.world;

import junit.framework.TestCase;


public class MoveableObjectTest extends TestCase {
	
	private MoveableObject notMovedObj;
	private MoveableObject movedObj;
	
	protected void setUp() throws Exception {
		this.movObjA = new MoveableObject();
		this.movObjB = new MoveableObject();
		this.movObjC = new MoveableObject();
		
		movObjB.updatePosition(new Point(1.0, 2.0));
		Thread.sleep(500);
		movObjB.updatePosition(new Point(3.0, 4.0));
		Thread.sleep(500);
		movObjB.updatePosition(new Point(5.0, 6.0));
	}

	public void testGetPosition() {
		Point positionA = movObjA.getPosition();
		Point positionB = movObjB.getPosition();
		assertEquals(new Point(0.0, 0.0), positionA);
		assertEquals(new Point(5.0, 6.0), positionB);
	}
	
	public void testUpdatePosition() {
		Point positionC = new Point(3.0, 1.0);
		movObjC.updatePosition(positionC);
		assertEquals(movObjC.getPosition(), positionC);
	}
	
	public void testGetSpeed() {
		double speedB = movObjB.getSpeed();
		// If exactly 500ms between adding latest two points, speed should be 25.6
		assertTrue(speedB > 24.7 && speedB < 25.7); // Allow for minor time differences
	}
	
	public void testVectorTo() {
		Vector vecAB = movObjA.vectorTo(movObjB);
		assertEquals(new Vector(5.0, 6.0), vecAB);
	}
	
	public void testIsMoving() {
		assertTrue(movObjA.isMoving, false);
		assertTrue(movObjB.isMoving, true);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
