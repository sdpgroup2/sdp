package group2.sdp.pc.comms;

import java.io.IOException;

public interface CommInterface {
	public int move(int direction, int angle, int speed) throws IOException;
	
}
