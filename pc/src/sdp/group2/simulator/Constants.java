package sdp.group2.simulator;

import sdp.group2.geometry.PointSet;
import sdp.group2.world.IPitch;
import sdp.group2.world.Pitch;

public class Constants {
	
	public static PointSet getDefaultPitchOutline() {
		PointSet pitchOutline = new PointSet();
		
		pitchOutline.add(130, 0);
		pitchOutline.add(1935, 0);
		pitchOutline.add(2065, 225);
		pitchOutline.add(2065, 825);
		pitchOutline.add(1935, 1150);
		pitchOutline.add(130, 1150);
		pitchOutline.add(0, 825);
		pitchOutline.add(0, 225);
		
		return pitchOutline;
	}
	
	public static PointSet[] getDefaultZoneOutlines() {
		PointSet[] zones = new PointSet[4];
		
		for (int i = 0; i < 4; i++) {
			zones[i] = new PointSet();
		}
		
		zones[0].add(130, 50);
		zones[0].add(330, 50);
		zones[0].add(330, 1100);
		zones[0].add(130, 1100);
		zones[0].add(50, 875);
		zones[0].add(50, 275);
		
		zones[1].add(517, 50);
		zones[1].add(922, 50);
		zones[1].add(922, 1100);
		zones[1].add(517, 1100);
		
		zones[2].add(1119, 50);
		zones[2].add(1527, 50);
		zones[2].add(1527, 1100);
		zones[2].add(1119, 1110);
		
		zones[3].add(1735, 50);
		zones[3].add(1935, 50);
		zones[3].add(2065, 275);
		zones[3].add(2065, 875);
		zones[3].add(1935, 1100);
		zones[3].add(1735, 1100);
		
		return zones;
	}
	
	public static Pitch getDefaultPitch() {
		Pitch pitch = new Pitch();
		
		pitch.setOutline(Constants.getDefaultPitchOutline());
		pitch.setAllZonesOutlines(Constants.getDefaultZoneOutlines());
		//pitch.updateRobotState(id, p, theta)
		initializeBall(pitch, -1);
		return pitch;
	}
	
	public static void initializeBall(IPitch pitch, double multiplier) {
		
		double x = 1075.0, y = 575.0;
		
		for (int i = 0; i < 10; i++) {
			pitch.updateBallPosition(x, y);
			x += 35.0 * multiplier;
		}
		
	}
	
}
