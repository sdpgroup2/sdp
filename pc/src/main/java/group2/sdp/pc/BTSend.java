package group2.sdp.pc;

import java.io.*;

import lejos.pc.comm.*;

/**
 * This is a PC sample. It connects to the NXT, and then
 * sends an integer and waits for a reply, 100 times.
 * 
 * Compile this program with javac (not nxjc), and run it 
 * with java.
 * 
 * You need pccomm.jar and bluecove.jar on the CLASSPATH. 
 * On Linux, you will also need bluecove-gpl.jar on the CLASSPATH.
 * 
 * Run the program by:
 * 
 *   java BTSend 
 * 
 * Your NXT should be running a sample such as BTReceive or
 * SignalTest. Run the NXT program first until it is
 * waiting for a connection, and then run the PC program. 
 * 
 * @author Gordon Edwards 
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
	
	public static void turnForwardMessage(Double degrees) throws IOException {
		initialise();
		dos.writeChars("turn " + degrees);
		closeStreams();
	} 
	
	private static void initialise() {
		conn = new NXTConnector();

		// Connect to any NXT over Bluetooth
		boolean connected = conn.connectTo("btspp://");
	   
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
