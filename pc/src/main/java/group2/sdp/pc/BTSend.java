package group2.sdp.pc;

import java.io.*;

import lejos.pc.comm.*;

/**
 * Calling methods in this class will send messages to device through a stream received in the BTReceive class, and prompts
 * actions in the robot package
 * @author Gordon Edwards
 * @author Michael Mair
 * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843
 */
public class BTSend {   
	private OutputStream outStream;
	private InputStream inStream;
	private NXTComm comm;
	private boolean connected = false;
	private int buffer = 0;
	private NXTInfo nxtInfo;
	
	
	public BTSend(String robotName, String robotMacAddress){
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, robotName,
				robotMacAddress);
	}
	
	public void forward(int speed) throws IOException {
		
	} 
	
	public void setSpeed(int speed) throws IOException {

	}
	
	public void arc(int speed) throws IOException {

	}
	public int rotate(int direction, int angle, int speed) throws IOException {
		int[] command = { Commands.ROTATE, direction, angle, speed };
		int confirmation = 0;
		try {
			confirmation = sendToRobot(command);
		} catch (IOException e1) {
			System.out.println("Could not send command");
			e1.printStackTrace();
		}
		System.out.println("Rotate...");
		return confirmation;
	}
	public int stop() {
		int[] command = { Commands.STOP, 0, 0, 0 };
		int confirmation = 0;
		try {
			confirmation = sendToRobot(command);
		} catch (IOException e1) {
			System.out.println("Could not send command");
			e1.printStackTrace();
		}
		System.out.println("Stop...");
		return confirmation;
	}
	
	public int kick(int speed) throws IOException {

		
		int[] command = { Commands.KICK, 0, 0, 0 };
		int confirmation = 0;
		try {
			confirmation = sendToRobot(command);
		} catch (IOException e1) {
			System.out.println("Could not send command");
			e1.printStackTrace();
		}
		System.out.println("Kick");
		return confirmation;
		
	}
	public void openBluetoothConn(String robotName) throws IOException {
		
		comm = null;
		try {
			comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.err.println("Could not create connection: " + e.toString());
		}

		System.out.println("Attempting to connect to robot...");

		try {
			comm.open(nxtInfo);
			outStream = (OutputStream) comm.getOutputStream();
			inStream = (InputStream) comm.getInputStream();   
		} catch (NXTCommException e) {
			throw new IOException("Failed to connect " + e.toString());
		} 
   }
	
	public void closeBluetoothConn() {
		try {
	    	inStream.close();
	    	outStream.close();
	    	comm.close();
	    	connected = false;
	   	} catch (IOException ioe) {
	    	System.out.println("IOException closing connection:");
	    	System.out.println(ioe.getMessage());
	    }
	}
	
	public int sendToRobot(int[] comm) throws IOException {
		if (!connected)
			return -3;
		if (buffer < 2) {
			byte[] command = { (byte) comm[0], (byte) comm[1], (byte) comm[2],
					(byte) comm[3] };

			outStream.write(command);
			outStream.flush();
			buffer += 1;
		} else {
			// The buffer is full we can't send a package;
			return -1;
		}

		int[] confirmation;
		try {
			confirmation = receiveFromRobot();
			if (confirmation[1] == comm[0]) {
				buffer -= 1;
				return confirmation[1];
			}
		} catch (IOException e1) {
			System.out.println("Could not receive confirmation");
			buffer -= 1;
			return -2;
		}
		System.out.println("Buffer is full, command not sent!");
		return -2;
	}
	
	public int[] receiveFromRobot() throws IOException {
		byte[] res = new byte[4];
		inStream.read(res);
		int[] ret = { (int) res[0], (int) res[1], (int) res[2],
				(int) res[3] };
		return ret;
	}
}
