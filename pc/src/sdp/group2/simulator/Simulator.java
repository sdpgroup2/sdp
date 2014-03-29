//package sdp.group2.simulator;
//
//import java.util.Random;
//
//import sdp.group2.world.Pitch;
//
//
//public class Simulator implements Runnable {
//
//	private static final long ITERATION_MS = 250L;
//	private static final boolean visualize = true;
//	private boolean running = true;
//	private Pitch pitch;
//	
//	public Simulator(Pitch pitch) {
//		this.pitch = pitch;
//		if (visualize) {
//			new Visualizator(pitch);
//		}
//	}
//	
//	public void start() {
//		// check if ball in the goal
//		Random rand = new Random();
//		double direction = rand.nextDouble() < 0.5 ? -1.0 : 1.0;
//		Constants.initializeBall(pitch, direction);
//	}
//	
//	public void iterate() {
//		// if in the enemy offensive zone kick the ball to score
//		// if in the enemy defence zone kick to pass to the attacker
//		// iterate the state
//		running = false;
//	}
//	
//	public void run() {
//		while(true) {
//			if (!running) {
//				break;
//			}
//			
//			iterate();
//			
//			try {
//				wait(ITERATION_MS);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//}
