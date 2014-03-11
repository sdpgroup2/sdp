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
	
	public final static short DO_NOTHING = 0;
	public final static short FORWARDS = 1;
	public final static short BACKWARDS = 2;
	public final static short ANGLEMOVE = 3;
	public final static short ROTATE = 4;
	public final static short STOP = 5;
	public final static short KICK = 6;
	public final static short STEER = 7;
	public final static short OPENKICKER = 8;
	public final static short CLOSEKICKER = 9;
	public final static short ROTATEKICKER = 10;
	
	public final static short FORCEQUIT = 63;
	public final static short DISCONNECT = 64;
	

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
		case FORCEQUIT:		return "QUIT";
		case DISCONNECT:	return "DISCONNECT";
		default: return "";
		}
	}
}
