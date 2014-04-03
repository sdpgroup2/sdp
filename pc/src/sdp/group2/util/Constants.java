package sdp.group2.util;

import sdp.group2.geometry.Point;

public class Constants {

	/** Robot names and MACs */
    public static final String ROBOT_2A_NAME = "SDP2A";
    public static final String ROBOT_2A_MAC = "00165307D55F";
    public static final String ROBOT_2D_NAME = "SDP 2D";
    public static final String ROBOT_2D_MAC = "0016530BBBEA";

    /** Robot configuration */
    public static final int ATK_KICK_ANGLE = 45;
    public static final int DEF_KICK_ANGLE = 45;
    public static final int ATK_KICK_POWER = Integer.MAX_VALUE;
    public static final int DEF_KICK_POWER = 200;
    public static final int ATK_MOVE_SPEED = 1000;
    public static final int DEF_MOVE_SPEED = 1000;

    public static final double PX_TO_MM = 4.56310;
    public static final double MM_TO_PX = 0.2191;
    
    // Center of the pitches in millimeters
    public static final Point PITCH0_CENTER = new Point(0, 0);
    public static final Point PITCH1_CENTER = new Point(1282, 757);
    
    // Height of the camera in millimeters
//    public static final double PITCH0_CAMERA_HEIGHT = 2370;
    public static final double PITCH0_CAMERA_HEIGHT = 2438.4;
    public static final double PITCH1_CAMERA_HEIGHT = 2285;
    
    // Height of the robot in millimetersprivate Point position;
    public static final double ROBOT_HEIGHT = 175;
    public static final double BALL_HEIGHT = 51;
    
    // Lines in millimetres
    public static final int[] MAIN_LINES = { 485, 1075, 1620};
    public static final int[] SIDE_LINES = { 473, 1070, 1637};
    
    // Approx distance from centre of baseplate to dot
    public static final int BASEPLATE_RADIUS = 12;
    
    public static int HISTORY_SIZE = 15;
    public static int STABLE_DISTANCE = 120;

    public enum TeamColour {
        YELLOW,
        BLUE;

        public static TeamColour valueOf(int teamColour) throws IllegalArgumentException {
            switch (teamColour) {
                case 0: {
                    return YELLOW;
                }
                case 1: {
                    return BLUE;
                }
                default: {
                    throw new IllegalArgumentException("TeamColour can be 0 or 1.");
                }
            }
        }
    }

    public enum PitchType {
        MAIN,
        SIDE;
        public static PitchType valueOf(int pitchType) throws IllegalArgumentException {
            switch (pitchType) {
                case 0: {
                    return MAIN;
                }
                case 1: {
                    return SIDE;
                }
                default: {
                    throw new IllegalArgumentException("PitchType can be 0 or 1.");
                }
            }
        }
    }

    public enum TeamSide {
        LEFT,
        RIGHT
    }

 
}
