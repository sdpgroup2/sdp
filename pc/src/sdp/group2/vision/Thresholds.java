package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;

public class Thresholds {
	
	public static Thresholds defaultThresholds = new Thresholds(
			new int[]{-10, 92, 140}, 
			new int[]{10, 256, 256},
			new int[] {30, 22, 50},
		    new int[] {80, 130, 140},
		    new int[] {40, 60, 150},
		    new int[] {80, 160, 255},    
		    new int[] {20, 100, 100}, 
		    new int[] {30, 255, 255} 
			);
	
	public static Thresholds activeThresholds = defaultThresholds;
	
	public final int[] ballMins;
	public final int[] ballMaxs;
	public final int[] dotMins;
	public final int[] dotMaxs;
	public final int[] basePlateMins;
	public final int[] basePlateMaxs;
	public final int[] yellowMins;
	public final int[] yellowMaxs;
	
	public Thresholds(int[] ballMins, int[] ballMaxs, int[] dotMins,
			int[] dotMaxs, int[] basePlateMins, int[] basePlateMaxs,
			int[] yellowMins, int[] yellowMaxs) {
		super();
		this.ballMins = ballMins;
		this.ballMaxs = ballMaxs;
		this.dotMins = dotMins;
		this.dotMaxs = dotMaxs;
		this.basePlateMins = basePlateMins;
		this.basePlateMaxs = basePlateMaxs;
		this.yellowMins = yellowMins;
		this.yellowMaxs = yellowMaxs;
	}
//	public Thresholds(int[] ballMins, int[] ballMaxs) {
//		this.ballMins = ballMins;
//		this.ballMaxs = ballMaxs;
//		this.dotMins = dt
//	}
}
