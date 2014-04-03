package sdp.group2.comms;

/**
 * Opcodes for commands.
 * Each command should be an short[] made up of 4 bytes.
 * First byte - opcode
 * Remaining three bytes will be options for the commands
 * @author Michael Mair
 * Based on code by SDP Group 4 2013
 *
 */
public class Commands {
	
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
	public final static int KICK360 = 11;
	
	public final static int CLEAR = 62;
	public final static int FORCEQUIT = 63;
	public final static int DISCONNECT = 64;
	

	public static String getInitial(short command) {
		switch (command) {
		case DO_NOTHING: 	return "STAY";
		case FORWARDS: 		return "FW";
		case BACKWARDS: 	return "BK";
		case ANGLEMOVE: 	return "MV";
		case ROTATE:		return "ROT";
		case STOP:			return "STOP";
		case KICK:			return "KICK";
		case STEER:			return "STR";
		case OPENKICKER:	return "OPENK";
		case CLOSEKICKER:	return "CLOSEK";
		case ROTATEKICKER:	return "ROTK";
		case CLEAR:			return "CLEAR";
		case FORCEQUIT:		return "QUIT";
		case DISCONNECT:	return "DISCONNECT";
		case KICK360: 		return "360KICK";
		default: return "";
		}
	}
}
