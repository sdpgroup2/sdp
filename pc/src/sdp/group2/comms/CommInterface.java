package sdp.group2.comms;

import java.io.IOException;

public interface CommInterface {
	
	/**
	 * 
	 * @param direction = 1 clockwise, -1 anti-clockwise
	 * @param speed
	 * @param distance in mm to travel
	 * @return 12 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot; 7 - lock
	 * @throws IOException
	 */

	public int move(int direction, int speed, int distance) throws IOException;
	
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 6 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot; 7 - lock
	 * @throws IOException
	 */
	public int rotate(int angle, int speed) throws IOException;
	
	/**
	 * 
	 * @param angle
	 * @param speed
	 * @return 4 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot; 7 - lock
	 * @throws IOException
	 */
	public int kick(int angle, int speed) throws IOException;
	
	/**
	 * 
	 * @return 3 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot; 7 - lock
	 * @throws IOException
	 */
	public int stop();
	
	/**
	 * 
	 * @param turnRate
	 * @return 36 - sent successfully; -1 - buffer full; -2 - confirmation failed;
	 * 		   -3 - not connected to robot; 7 - lock
	 * @throws IOException
	 */
	public int steer(int turnRate) throws IOException;
	
	/**
	 * @throws IOException
	 */
	public void disconnect();
	
	/**
	 * @throws IOException
	 */
	public void forcequit();
	
}
