package group2.sdp.pc.comms;

import java.io.IOException;

public interface CommInterface {
	
	/**
	 * 
	 * @param direction = 1 clockwise, -1 anti-clockwise
	 * @param speed
	 * @return 12 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot
	 * @throws IOException
	 */
	public int move(short direction, short speed, short distance) throws IOException;
	
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 6 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot
	 * @throws IOException
	 */
	public int rotate(short angle, short speed) throws IOException;
	
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 4 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot
	 * @throws IOException
	 */
	public int kick(short angle, short speed) throws IOException;
	
	/**
	 * 
	 * @return 3 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot
	 * @throws IOException
	 */
	public int stop();
	
	/**
	 * 
	 * @param turnRate
	 * @return 36 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot
	 * @throws IOException
	 */
	public int steer(short turnRate) throws IOException;
	
	/**
	 * @throws IOException
	 */
	public void disconnect();
	
	/**
	 * @throws IOException
	 */
	public void forcequit();
	
}
