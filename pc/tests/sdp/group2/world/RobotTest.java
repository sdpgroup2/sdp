package sdp.group2.world;

import junit.framework.TestCase;

import org.mockito.MockitoAnnotations;

import sdp.group2.geometry.Point;


public class RobotTest extends TestCase {

	Robot rob;
  	Ball ball;

  	protected void setUp() throws Exception {
  		MockitoAnnotations.initMocks(this);
  	}

  	public void testGoTo() {
  		// Initialise
//  		rob = new Robot(new Point(3, 4), new Point(2, 4),  new Zone(0));
//  		this.ball = new Ball(new Point(0, 0));
//		
//		// call a method
//  		System.out.println(rob.getPosition());
//		rob.goTo(ball, true);
//		
//		// Test that we rotate the right amount
//		Mockito.verify(rob).rotate(-45);
//		// Test that we go forward.
//		Mockito.verify(rob).forward(1, 5);
  	}
  	  	
  	public void testGetSmallerAngle() {
  		double result;
  		
  		this.rob = new Robot(new Point(3, 3), new Point(2, 3),  new Zone(0));
  		this.ball = new Ball(new Point(2, 2));
  		
  		result = rob.smallerAngle(ball.getPosition());
  		assertEquals(-45.0, result);
  		
  		this.rob = new Robot(new Point(3, 3), new Point(2, 3),  new Zone(0));
  		this.ball = new Ball(new Point(2, 4));
  		
  		result = rob.smallerAngle(ball.getPosition());
  		assertEquals(45.0, result);
  		
  		this.rob = new Robot(new Point(3, 3), new Point(2, 3),  new Zone(0));
  		this.ball = new Ball(new Point(4, 2));
  		
  		result = rob.smallerAngle(ball.getPosition());
  		assertEquals(45.0, result);
  		
  		this.rob = new Robot(new Point(3, 3), new Point(2, 3),  new Zone(0));
  		this.ball = new Ball(new Point(4, 4));
  		
  		result = rob.smallerAngle(ball.getPosition());
  		assertEquals(-45.0, result);
  		
  		this.rob = new Robot(new Point(3, 3), new Point(4, 3),  new Zone(0));
  		this.ball = new Ball(new Point(2, 2));
  		
  		result = rob.smallerAngle(ball.getPosition());
  		assertEquals(-45.0, result);
  		
  	}

  	public void testGoToBackward() {
  		// Initialise
//  		this.rob = new Robot(new Point(1, 1), new Point(0, 1),  new Zone(0));
//  		this.ball = new Ball(new Point(5, 1));
//		
//		// call a method
//		rob.goTo(ball, false);
//		
//		// Verify that rotate is not called
////		Mockito.verify(rob).rotate(-45);
////		Mockito.verify(rob.rotate, never()).someMethod()
//		// Test that we go backward.
//		Mockito.verify(rob).forward(-1, 4);
  	}
} 