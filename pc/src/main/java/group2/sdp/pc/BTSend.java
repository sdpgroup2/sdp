package group2.sdp.pc;

import java.io.*;

import lejos.pc.comm.*;

/**
 
 * Calling methods in this class will send messages to device through a stream received in the BTReceive class, and prompts
 * actions in the robot package
 * @author Gordon Edwards and Michael Mair
 * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843
 */
public class BTSend {   
	private static DataOutputStream dos;
	private static DataInputStream dis;
	private static NXTConnector conn;
	
	public static void sendForwardMessage() throws IOException {
		initialise();
		dos.writeChars("forward");
		closeStreams();
	} 
	
	public static void turnMessage(Double degrees) throws IOException {
		initialise();
		dos.writeChars("turn " + degrees);
		closeStreams();
	} 
	
	public static void setSpeedMessage(Double speed) throws IOException {
		initialise();
		dos.writeChars("speed " + speed);
		closeStreams();
	}
	
	public static void setAccMessage(Double speed) throws IOException {
		initialise();
		dos.writeChars("speed " + speed);
		closeStreams();
	}
	
	private static void initialise() {
		conn = new NXTConnector();

		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://SDP2A");
	   
		if (!connected) {
			System.err.println("Failed to connect to any NXT");
			System.exit(1);
		}

		dos = (DataOutputStream) conn.getOutputStream();
		dis = (DataInputStream) conn.getInputStream();            
   }
	
	private static void closeStreams() {
		try {
	    	dis.close();
	    	dos.close();
	    	conn.close();
	   	} catch (IOException ioe) {
	    	System.out.println("IOException closing connection:");
	    	System.out.println(ioe.getMessage());
	    }
	}
   
   public BTSend() {
	   
   }
}
