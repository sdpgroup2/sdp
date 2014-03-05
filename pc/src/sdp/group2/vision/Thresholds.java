package sdp.group2.vision;

public class Thresholds {
	
	public static Thresholds sidePitchThresholds = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 92, 140}, 
			new int[]{10, 256, 256},
			
			// dot mins/maxs
			new int[] {30, 22, 50},
		    new int[] {80, 130, 140},
		    
		    // baseplate mins/maxs
		    new int[] {40, 60, 150},
		    new int[] {80, 160, 255},
		    
		    // yellow mins/maxs
		    new int[] {20, 100, 100}, 
		    new int[] {30, 255, 255} 
	);
	
	public static Thresholds mainPitchThresholds = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 92, 140}, //TODO Get values
			new int[]{10, 256, 256}, //TODO Get values
			
			// dot mins/maxs
			new int[] {30, 30, 100},
			new int[] {90, 155, 155},
		    
		    // baseplate mins/maxs
		    new int[] {65, 135, 175},
		    new int[] {80, 204, 250},
		    
		    // yellow mins/maxs
		    new int[] {20, 120, 200}, 
		    new int[] {40, 180, 256}
	);
	
	public static Thresholds activeThresholds = sidePitchThresholds;
	
	public int[] ballMins;
	public int[] ballMaxs;
	public int[] dotMins;
	public int[] dotMaxs;
	public int[] basePlateMins;
	public int[] basePlateMaxs;
	public int[] yellowMins;
	public int[] yellowMaxs;
	
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

}
