package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvRect;

import java.awt.image.CropImageFilter;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class Thresholds {
	
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
		System.out.println(ballMins[2]);
		this.ballMaxs = ballMaxs;
		this.dotMins = dotMins;
		this.dotMaxs = dotMaxs;
		System.out.println( dotMaxs[1]);
		this.basePlateMins = basePlateMins;
		this.basePlateMaxs = basePlateMaxs;
		this.yellowMins = yellowMins;
		this.yellowMaxs = yellowMaxs;
		this.yellowPixelsThreshold = yellowPixelsThreshold;
		this.rect = rect;
		System.out.printf("vals %d %d %d %d", rect[0],rect[1],rect[2],rect[3]);
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
