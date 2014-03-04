package sdp.navigation.simplepathfinding;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sdp.geom.Circle2D;


public class SimplePathTest {
	
	private SimplePath mSimplePath;

	@Before
	public void setUp() throws Exception {
		mSimplePath = new SimplePath(new Rectangle(0, 0, 50, 50));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getPath_WhenHasNoObjects_DirectLine() {
		Collection<Point> path = mSimplePath.getPath(new LinkedList<Circle2D>(), new Point(1, 1), new Point(2,2));
		assertNotNull(path);
		assertEquals(2, path.size());
		
		Point p1 = (Point) path.toArray()[0];
		Point p2 = (Point) path.toArray()[1];
		
		assertEquals(1, p1.x);
		assertEquals(1, p1.y);
		
		assertEquals(2, p2.x);
		assertEquals(2, p2.y);
	}

	@Test
	public void getPath_WhenHasOneObjects_SmallPath() {
		List<Circle2D> object = Arrays.asList(new Circle2D(new Point2D.Double(10, 10), 5));
		Collection<Point> path = mSimplePath.getPath(object, new Point(0, 0), new Point(20,20));
		assertNotNull(path);
		assertEquals(3, path.size());
		
		Point p1 = (Point) path.toArray()[0];
		Point p2 = (Point) path.toArray()[2];
		
		assertEquals(0, p1.x);
		assertEquals(0, p1.y);
		
		assertEquals(20, p2.x);
		assertEquals(20, p2.y);
	}
	
}
