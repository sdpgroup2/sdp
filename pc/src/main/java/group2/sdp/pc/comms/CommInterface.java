package group2.sdp.pc.comms;

import java.io.IOException;

public interface CommInterface {
	/**
	 * 
	 * @param direction = 1 clockwise, -1 anti-clockwise
	 * @param speed
	 * @return 12 if sent, -1 or -2 if failed
	 * @throws IOException
	 */
	public int move(int direction, int speed) throws IOException;
	
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 6 if sent, -1 or -2 if failed
	 * @throws IOException
	 */
	public int rotate(int angle, int speed) throws IOException;
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 4 if sent, -1 or -2 if failed
	 * @throws IOException
	 */
	public int kick(int angle, int speed) throws IOException;
	/**
	 * 
	 * @return 3 if sent, -1 or -2 if failed
	 * @throws IOException
	 */
	public int stop();
	/**
	 * 
	 * @param turnRate
	 * @return 36 if sent, -1 or -2 if failed
	 * @throws IOException
	 */
	public int steer(double turnRate) throws IOException;
	/**
	 * @throws IOException
	 */
	public void disconnect();
	/**
	 * @throws IOException
	 */
	public void forcequit();
	
}
