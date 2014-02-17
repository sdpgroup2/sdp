package group2.sdp.pc.world;

public class Constants {

	public static final int MAIN_PITCH = 1;
	public static final int SIDE_PITCH = -1;
	public static final int YELLOW_TEAM = 1;
	public static final int BLUE_TEAM = -1;
	
	public static final String ROBOT_2A_NAME = "SDP2A";
	public static final String ROBOT_2A_MAC = "00165307D55F";
	public static final String ROBOT_2D_NAME = "SDP 2D";
	public static final String ROBOT_2D_MAC = "0016530BBBEA";
	

	public static final double PX_TO_MM = 4.56310;
	public static final double MM_TO_PX = 0.2191;

	public enum TeamColor {
		YELLOW,
		BLUE
	}

	public enum PitchType {
		MAIN,
		SIDE
	}

}
