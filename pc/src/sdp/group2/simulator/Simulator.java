package sdp.group2.simulator;

import sdp.group2.world.Pitch;

public class Simulator extends Pitch implements Runnable {

	private static final long ITERATION_MS = 250L;
	
	// if in the enemy offensive zone kick the ball to score
	// if in the enemy defence zone kick to pass to the attacker
	// iterate the state
	
	public void iterate() {
		
	}
	
	public void run() {
		try {
			iterate();
			wait(ITERATION_MS);
		} catch (InterruptedException e)
		{ e.printStackTrace(); }
	}
	
}
