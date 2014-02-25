package sdp.group2.util;

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

    /** What is "Ello Gov'nor"??? Is it Elvish?" */
    // Ello Gov'nor
    public enum TeamColour {
        YELLOW,
        BLUE;

        /** Why you do enum if you use valueOf? */
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

        /** Why you do enum if you use valueOf ? */
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

    /** Blame: What is LEFT what is RIGHT, my hand, my leg, the can of beer on the table?
     *  	   Please, comment it properly. */
    public enum TeamSide {
        LEFT,
        RIGHT
    }

}
