//package sdp.group2.world;
//
//import sdp.group2.geometry.Point;
//import sdp.group2.geometry.Vector;
//import junit.framework.TestCase;
//
//
//public class MovableObjectTest extends TestCase {
//	
//	private MovableObject movObjA;
//	private MovableObject movObjB;
//	private MovableObject movObjC;
//	
//	protected void setUp() throws Exception {
//		this.movObjA = new MovableObject();
//		this.movObjB = new MovableObject();
//		this.movObjC = new MovableObject();
//		
//		movObjB.updatePosition(new Point(1.0, 2.0));
//		Thread.sleep(500);
//		movObjB.updatePosition(new Point(3.0, 4.0));
//		Thread.sleep(500);
//		movObjB.updatePosition(new Point(5.0, 6.0));
//	}
//
//	public void testGetPosition() {
//		Point positionA = movObjA.getPosition();
//		Point positionB = movObjB.getPosition();
//		assertEquals(new Point(0.0, 0.0), positionA);
//		assertEquals(new Point(5.0, 6.0), positionB);
//	}
//	
//	public void testUpdatePosition() {
//		Point positionC = new Point(3.0, 1.0);
//		movObjC.updatePosition(positionC);
//		assertEquals(movObjC.getPosition(), positionC);
//	}
//	
//	public void testGetSpeed() {
//		double speedB = movObjB.getSpeed();
//		// If exactly 500ms between adding latest two points, speed should be 25.6
//		assertTrue(speedB > 24.7 && speedB < 25.7); // Allow for minor time differences
//	}
//	
//	public void testVectorTo() {
//		Vector vecAB = movObjA.vectorTo(movObjB);
//		assertEquals(new Vector(5.0, 6.0), vecAB);
//	}
//	
//	public void testIsMoving() {
//		assertFalse(movObjA.isMoving());
//		assertTrue(movObjB.isMoving());
//	}
//
//}
