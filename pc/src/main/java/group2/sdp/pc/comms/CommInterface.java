package group2.sdp.pc.comms;

import java.io.IOException;

public interface CommInterface {
	
	public int move(int direction, int angle, int speed) throws IOException;
	
	public int rotate(int direction, int angle, int speed) throws IOException;
	
	public int kick(int angle, int speed) throws IOException;
	
	public int stop();
	
	public int steer() throws IOException;

	public void disconnect();
	
	public void forcequit();
	
}
