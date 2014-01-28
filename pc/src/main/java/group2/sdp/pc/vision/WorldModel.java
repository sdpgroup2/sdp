package group2.sdp.pc.vision;

import group2.sdp.pc.geom.VecF;


/**
 * Stores the known positions of all objects: robots and the ball.
 * Should be able to predict the position of an object even if
 * the vision system loses track of it temporarily.
 * 
 * @author Paul Harris
 */
public class WorldModel {

	protected WorldObject ball = new WorldObject("Ball");
	
	protected WorldObject[] blueRobots = new WorldObject[] {
		new WorldObject("Blue Robot 0"),
		new WorldObject("Blue Robot 1"),
	};
	
	protected WorldObject[] yellowRobots = new WorldObject[] {
			new WorldObject("Yellow Robot 0"),
			new WorldObject("Yellow Robot 1"),
	};
	
	public void updateBallPosition(VecF position) {
		ball.setPosition(position);
	}
	
}
