package group2.sdp.pc;

import java.io.*;

import com.ibm.oti.connection.btgoep.Connection;

import lejos.pc.comm.*;

/**
 
 * Calling methods in this class will send messages to device through a stream received in the BTReceive class, and prompts
 * actions in the robot package
 * @author Gordon Edwards
 * @author Michael Mair
 * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843
 */
public class BTSend {   
	private static OutputStream outStream;
	private static InputStream inStream;
	private static NXTConnector conn;
	private static boolean connected = false;
	private static int buffer = 0;
	
	public static void sendForwardMessage(String robotName) throws IOException {
		openBluetoothConn(robotName);

		closeStreams();
	} 
	
	public static void sendturnMessage(String robotName, Double degrees) throws IOException {
		openBluetoothConn(robotName);
		outStream.write("turn " + degrees);
		closeStreams();
	} 
	
	public static void sendSpeedMessage(String robotName, Double speed) throws IOException {
		openBluetoothConn(robotName);
		outStream.writeChars("speed " + speed);
		closeStreams();
	}
	
	public static void sendAccMessage(String robotName, Double speed) throws IOException {
		openBluetoothConn(robotName);
		outStream.writeChars("speed " + speed);
		closeStreams();
	}
	
	private static void openBluetoothConn(String robotName) {
		conn = new NXTConnector();

		// Connect to any NXT over Bluetooth
		
		connected = conn.connectTo("btspp://SDP 2D");
		
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}

		outStream = (OutputStream) conn.getOutputStream();
		inStream = (InputStream) conn.getInputStream();            
   }
	
	private static void closeStreams() {
		try {
	    	inStream.close();
	    	outStream.close();
	    	conn.close();
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
		int[] ret = { (int) (res[0]), (int) (res[1]), (int) (res[2]),
				(int) (res[3]) };
		return ret;
	}
}
