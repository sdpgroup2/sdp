package group2.sdp.pc;

import java.util.ArrayDeque;
import java.util.Deque;


public class Timer {
	
	private static final double A_BILLION = 1000000000.0;

	private long frames = 0;
	private int timesCount;
	private long lastTime;
	private Deque<Long> timeDiffs = new ArrayDeque<Long>();
	
	
	public Timer(int framesToAverage) {
		timesCount = framesToAverage;
		lastTime = System.nanoTime();
	}
	
	// Call this every frame to keep the timer updated.
	public void tick() {
		long time = System.nanoTime();
		timeDiffs.addFirst(time - lastTime);
		lastTime = time;
		if (timeDiffs.size() > timesCount) {
			timeDiffs.removeLast();
		}
		frames += 1;
	}
	
	// Reports the framerate every n frames where n is calloutFrequency.
	public void tick(int calloutFrequency) {
		tick();
		if (frames % calloutFrequency == 0) {
			System.out.printf("FPS: %f\n", frameRate());
		}
	}
	
	// The number of nanoseconds between frames.
	public long frameDelay() {
		long sum = 0;
		for (Long delta: timeDiffs) {
			sum += delta;
		}
		return sum / timesCount;
	}
	
	// The framerate in frames per second.
	public double frameRate() {
		return A_BILLION / frameDelay();
	}
}
