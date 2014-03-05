package sdp.group2.vision;

public class Thresholds {
	
	public static Thresholds sidePitchThresholds = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 92, 140}, 
			new int[]{10, 256, 256},
			
			// dot mins/maxs
			new int[] {26, 19, 43},
		    new int[] {65, 139, 135},
		    
		    // baseplate mins/maxs
		    new int[] {35, 50, 150},
		    new int[] {90, 180, 255},
		    
		    // yellow mins/maxs
		    new int[] {20, 90, 100}, 
		    new int[] {40, 255, 255},
		    
		    // yellow pixel threshold
		 	60
	);
	
	public static Thresholds spongeBobSquarePitch = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 92, 140}, 
			new int[]{10, 256, 256},
			
			// dot mins/maxs
			new int[] {20, 40, 90},
		    new int[] {60, 100, 142},
		    
		    // baseplate mins/maxs
		    new int[] {28, 72, 160},
		    new int[] {80, 160, 255},
		    
		    // yellow mins/maxs
		    new int[] {20, 100, 100}, 
		    new int[] {30, 255, 255},
		    
		    
		    // yellow pixel threshold
		 	60
	);	
	
	public static Thresholds mainPitchThresholds = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 80, 70},
			new int[]{10, 256, 256},
			
			// dot mins/maxs
			new int[] {30, 30, 100},
			new int[] {90, 155, 155},
		    
		    // baseplate mins/maxs
		    new int[] {65, 135, 175},
		    new int[] {80, 204, 250},
		    
		    // yellow mins/maxs
		    new int[] {20, 120, 200}, 
		    new int[] {40, 180, 256},
		    
		    // yellow pixel threshold
		    10
	);
	
	public static Thresholds nightMainPitchThresholds = new Thresholds(
			// ball mins/maxs
			new int[]{-10, 80, 70},
			new int[]{10, 256, 256},
			
			// dot mins/maxs
			new int[] {26, 55, 40},
			new int[] {100, 210, 60},
		    
		    // baseplate mins/maxs
            new int[] {45, 110, 77},
            new int[] {90, 240, 110},
		    
		    // yellow mins/maxs
            new int[] {20, 100, 100}, 
		    new int[] {30, 255, 255},
			
			// yellow pixel threshold
			10
	);
	
	public static Thresholds activeThresholds = mainPitchThresholds;
	
	
	public final int[] ballMins;
	public final int[] ballMaxs;
	public final int[] dotMins;
	public final int[] dotMaxs;
	public final int[] basePlateMins;
	public final int[] basePlateMaxs;
	public final int[] yellowMins;
	public final int[] yellowMaxs;
	public final int yellowPixelsThreshold;
	
	public Thresholds(int[] ballMins, int[] ballMaxs, int[] dotMins,
			int[] dotMaxs, int[] basePlateMins, int[] basePlateMaxs,
			int[] yellowMins, int[] yellowMaxs, int yellowPixelsThreshold) {
		super();
		this.ballMins = ballMins;
		this.ballMaxs = ballMaxs;
		this.dotMins = dotMins;
		this.dotMaxs = dotMaxs;
		this.basePlateMins = basePlateMins;
		this.basePlateMaxs = basePlateMaxs;
		this.yellowMins = yellowMins;
		this.yellowMaxs = yellowMaxs;
		this.yellowPixelsThreshold = yellowPixelsThreshold;
	}

}
