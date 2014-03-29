package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvRect;

import java.awt.image.CropImageFilter;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class Thresholds {
	
//	public static Thresholds sidePitchThresholds = new Thresholds(
//	        "Side Pitch",
//			// ball mins/maxs
//			new int[]{-10, 92, 140}, 
//			new int[]{10, 230, 230},
//			
//			// dot mins/maxs
//			new int[] {26, 19, 43},
//		    new int[] {65, 139, 135},
//		    
//		    // baseplate mins/maxs
//		    new int[] {40, 60, 150},
//		    new int[] {80, 160, 255},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 100, 100},
//		    new int[] {30, 255, 255},
//		    
//		    // yellow pixel threshold
//		 	120,
//		 	
//		 	// Cropping rectangle
//		 	cvRect(45, 90, 543, 300)
//	);
//	
//	public static Thresholds hikuaiThresholds = new Thresholds(
//	        "Hikuai",
//			// ball mins/maxs
//			new int[]{-10, 80, 120}, 
//			new int[]{10, 250, 250},
//			
//			// dot mins/maxs
//			new int[] {26, 19, 43},
//		    new int[] {65, 139, 135},
//		    
//		    // baseplate mins/maxs
//		    new int[] {20, 60, 100},
//		    new int[] {120, 200, 255},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 100, 100},
//		    new int[] {30, 255, 255},
//		    
//		    // yellow pixel threshold
//		 	30,
//		 	
//		 	// Cropping rectangle
//		 	cvRect(45, 90, 543, 300)
//	);
//	
//	public static Thresholds mainPitchThresholds = new Thresholds(
//			"Main Pitch",
//			// ball mins/maxs
//			new int[]{-10, 80, 70},
//			new int[]{10, 255, 255},
//			
//			// dot mins/maxs
//			new int[] {30, 30, 100},
//			new int[] {90, 155, 155},
//		    
//		    // baseplate mins/maxs
//		    new int[] {65, 135, 175},
//		    new int[] {80, 204, 250},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 120, 200}, 
//		    new int[] {40, 180, 255},
//		    
//		    // yellow pixel threshold
//		    10,
//		    
//		    // Cropping rectangle
//		 	cvRect(30, 60, 590, 310)
//	);
//	
//	public static Thresholds nightMainPitchThresholds = new Thresholds(
//	        "Night Main Pitch",
//			// ball mins/maxs
//			new int[]{-10, 80, 70},
//			new int[]{10, 255, 255},
//			
//			// dot mins/maxs
//			new int[] {26, 55, 40},
//			new int[] {100, 210, 60},
//		    
//		    // baseplate mins/maxs
//            new int[] {45, 110, 77},
//            new int[] {90, 240, 110},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 120, 200}, 
//		    new int[] {40, 180, 255},
//			
//			// yellow pixel threshold
//			10,
//			
//			// Cropping rectangle
//			new int[] {30, 60, 590, 310}
//	);
//	
//	public static Thresholds torosayThresholds = new Thresholds(
//			"Torosay",
//			// ball mins/maxs
//			new int[]{-10, 80, 70},
//			new int[]{10, 255, 255},
//			
//			// dot mins/maxs
//			new int[] {26, 55, 40},
//			new int[] {100, 210, 60},
//		    
//		    // baseplate mins/maxs
//            new int[] {45, 110, 77},
//            new int[] {90, 240, 183},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 76, 81}, 
//		    new int[] {53, 255, 255},
//			
//			// yellow pixel threshold
//			30,
//			
//			// Cropping rectangle
//			new int[] {30, 60, 590, 310}
//	);
//	
//	public static Thresholds rothesayThresholds = new Thresholds(
//			"Rothesay",
//			// ball mins/maxs
//			new int[]{-10, 80, 70},
//			new int[]{10, 255, 255},
//			
//			// dot mins/maxs
//			new int[] {31,28,89},
//			new int[] {95, 189, 250},
//		    
//		    // baseplate mins/maxs
//            new int[] {45, 110, 77},
//            new int[] {90, 240, 183},
//		    
//		    // yellow mins/maxs
//		    new int[] {20, 76, 81}, 
//		    new int[] {53, 255, 255},
//			
//			// yellow pixel threshold
//			100,
//			
//			// Cropping rectangle
//			new int[] {30, 60, 590, 310}
//	);
	
	public static Thresholds activeThresholds;
	
	public String name;
	public int[] ballMins;
	public int[] ballMaxs;
	public int[] dotMins;
	public int[] dotMaxs;
	public int[] basePlateMins;
	public int[] basePlateMaxs;
	public int[] yellowMins;
	public int[] yellowMaxs;
	public int yellowPixelsThreshold;
	public CvRect cropRect;
	public int[] rect;
	public EntityThresh[] entities = new EntityThresh[4];
	
	public Thresholds(String name, int[] ballMins, int[] ballMaxs, int[] dotMins,
			int[] dotMaxs, int[] basePlateMins, int[] basePlateMaxs,
			int[] yellowMins, int[] yellowMaxs, int yellowPixelsThreshold,
			int[] rect) {
		this.name = name;
		this.ballMins = ballMins;
		this.ballMaxs = ballMaxs;
		this.dotMins = dotMins;
		this.dotMaxs = dotMaxs;
		this.basePlateMins = basePlateMins;
		this.basePlateMaxs = basePlateMaxs;
		this.yellowMins = yellowMins;
		this.yellowMaxs = yellowMaxs;
		this.yellowPixelsThreshold = yellowPixelsThreshold;
		this.rect = rect;
		this.cropRect = cvRect(rect[0],rect[1],rect[2],rect[3]);
		
		EntityThresh ball = new EntityThresh("Ball");
		EntityThresh dot = new EntityThresh("Dot");
		EntityThresh basePlate = new EntityThresh("BasePlate");
		EntityThresh yellow = new EntityThresh("Yellow");
		
		ball.mins = ballMins;
		ball.maxs = ballMaxs;
		dot.mins = dotMins;
		dot.maxs = dotMaxs;
		basePlate.mins = basePlateMins;
		basePlate.maxs = basePlateMaxs;
		yellow.mins = yellowMins;
		yellow.maxs = yellowMaxs;
		
		entities[0] = ball;
		entities[1] = dot;
		entities[2] = basePlate;
		entities[3] = yellow;
		
	}

}
