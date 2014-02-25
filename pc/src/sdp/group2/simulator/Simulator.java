package sdp.group2.simulator;

import sdp.group2.world.Pitch;

public class Simulator extends Pitch implements Runnable {

	private static final long ITERATION_MS = 250L;
	private static Simulator instance = null;
	private final boolean running = true;
	
	private Simulator() {
		initialize();
	}
	
	public void initialize() {
		setOutline(Constants.getDefaultPitchOutline());
		setAllZonesOutlines(Constants.getDefaultZoneOutlines());
	}
	
	public Simulator getInstance() {
		if (instance == null) {
			instance = new Simulator();
		}
		
		return instance;
	}
	
	public void iterate() {
		// if in the enemy offensive zone kick the ball to score
		// if in the enemy defence zone kick to pass to the attacker
		// iterate the state
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
