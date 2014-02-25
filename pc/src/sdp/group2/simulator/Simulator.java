package sdp.group2.simulator;

import sdp.group2.world.IPitch;

public class Simulator implements Runnable {

	private static final long ITERATION_MS = 250L;
	private static final boolean visualize = true;
	private boolean running = true;
	private IPitch pitch;
	private Visualizator screen = null;
	
	
	public Simulator(IPitch pitch) {
		this.pitch = pitch;
		if (visualize) {
			new Visualizator(pitch);
		}
	}
	
	public void iterate() {
		// if in the enemy offensive zone kick the ball to score
		// if in the enemy defence zone kick to pass to the attacker
		// iterate the state
		running = false;
	}
	
	public void run() {
		while(true) {
			if (!running) {
				break;
			}
			
			iterate();
			
			try {
				wait(ITERATION_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
