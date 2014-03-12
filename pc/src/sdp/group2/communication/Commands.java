package sdp.group2.communication;

import java.io.IOException;

/**
 * Opcodes for commands.
 * Each command should be an int[] made up of 4 bytes.
 * First byte - opcode
 * Remaining three bytes will be options for the commands.
 * @author Michael Mair
 * Based on code by SDP Group 4 2013
 */
public class Commands{
	
	public final static int DO_NOTHING = 0;
	public final static int FORWARDS = 1;
	public final static int BACKWARDS = 2;
	public final static int ANGLEMOVE = 3;
	public final static int ROTATE = 4;
	public final static int STOP = 5;
	public final static int KICK = 6;
	public final static int STEER = 7;
	public final static int OPENKICKER = 8;
	public final static int CLOSEKICKER = 9;
	public final static int ROTATEKICKER = 10;
	
	public final static int CLEAR = 62;
	public final static int FORCEQUIT = 63;
	public final static int DISCONNECT = 64;
	

	public static String getName(int command) {
		switch (command) {
		case DO_NOTHING: 	return "Do nothing";
		case FORWARDS: 		return "Forwards";
		case BACKWARDS: 	return "Backwards";
		case ANGLEMOVE: 	return "Anglemove";
		case ROTATE:		return "Rotate";
		case STOP:			return "Stop";
		case KICK:			return "Kick";
		case STEER:			return "Steer";
		case OPENKICKER:	return "Open kicker";
		case CLOSEKICKER:	return "Close kicker";
		case ROTATEKICKER:	return "Rotate kicker";
		case CLEAR:			return "Clear";
		case FORCEQUIT:		return "Force quit";
		case DISCONNECT:	return "Disconnect";
		default: return "";
		}
	}
	
	/** 
	 * @param direction = 1 clockwise, -1 anti-clockwise
	 * @param speed
	 * @param distance in mm to travel
	 */
	public static int[] move(int direction, int speed, int distance) {
		return new int[] {ANGLEMOVE,direction,speed,distance};
	}
	
	public static int[] rotate (int direction, int speed) {
		return new int[] {ROTATE,direction,speed, 0};
	}

	public static int[] kick(int angle, int speed) {
		return new int[] {KICK,angle,speed,0};
	}
	
	public static int[] closeKicker() {
		return new int[] {CLOSEKICKER, 0, 0, 0};
	}
	
	public static int[] rotateKicker() {
		return new int[] {ROTATEKICKER, 0, 0, 0};
	}
	
	public static int[] openKicker() {
		return new int[] {OPENKICKER, 0, 0, 0};
	}

	public static int[] steer(int turnRate){
		return new int[] {STEER,turnRate,0,0};
	}
	
	public static int[] disconnect() {
		return new int[] {DISCONNECT,0,0,0};
	}
	
	public static int[] clear() {
		return new int[] {CLEAR, 0, 0, 0};
	}
}
