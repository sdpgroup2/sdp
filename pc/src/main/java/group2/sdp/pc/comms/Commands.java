package group2.sdp.pc.comms;

/**
 * Opcodes for commands.
 * Each command should be an int[] made up of 4 bytes.
 * First byte - opcode
 * Remaining three bytes will be options for the commands.
 * @author Michael Mair
 * Based on code by SDP Group 4 2013
 */
public class Commands {
	public final static short DO_NOTHING = 0;
	public final static short FORWARDS = 1;
	public final static short BACKWARDS = 2;
	public final static short STOP = 3;
	public final static short KICK = 4;
	public final static short DISCONNECT = 5;
	public final static short ROTATE = 6;
	public final static short ROTATEMOVE = 7;
	public final static short TRAVEL_ARC = 8;
	public final static short ACCELERATE = 9;
	public final static short LEFT = 10;
	public final static short RIGHT = 11;
	public final static short ANGLEMOVE = 12;
	public final static short SLOWMOVE = 13;
	public final static short DRIBBLERON = 23;
	public final static short DRIBBLEROFF = 24;
	public final static short TEST = 66;
	public final static short FORCEQUIT = 55;
	public final static short BEEP = 42;
	public final static short ARC = 37;
	public final static short STEER = 36;
}
