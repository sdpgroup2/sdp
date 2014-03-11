package sdp.group2.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import lejos.nxt.LCD;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

/**
 * Calling methods in this class will send messages to device through a stream received in the BTReceive class, and prompts
 * actions in the robot package
 * @author Gordon Edwards
 * @author Michael Mair
 * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843
 * and from SDP Group 4 2013
 */
public class Sender {
	private OutputStream outStream;
	private InputStream inStream;
	private NXTComm comm;
	private boolean connected = false;
	private int buffer = 0;
	private NXTInfo nxtInfo;
	private boolean robotReady;
	private String robotName;

	public Sender(String robotName, String robotMacAddress) throws IOException{
		this.robotName = robotName;
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, robotName, robotMacAddress);
		openBluetoothConn(robotName);
	}
	
	public String getName(){
		return robotName;
	}
	
	/**
	 * Basic way of sending a command.
	 * @param command One of the constants from Commands
	 * @return A confirmation code.
	 */
	public synchronized int command(short[] command) throws IOException {
		int confirmation = attemptConnection(command);
		System.out.println("Command: "+Commands.getName((int) command[0]));
		return confirmation;
	}

	public synchronized void disconnect() {
		short[] command = { Commands.DISCONNECT, 0, 0, 0 };
		try {
			sendToRobot(command);
			// Give the command time to send - prevents brick crash
			Thread.sleep(100);
		} catch (IOException e) {
			System.out.println("Could not send command");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
			e.printStackTrace();
		}
		closeBluetoothConn();
		System.out.println("Quit... Please reconnect.");
	}


	public synchronized void forcequit() {
		short[] command = { Commands.FORCEQUIT, 0, 0, 0 };
		try {
			sendToRobot(command);
			// Give the command time to send - prevents brick crash
			Thread.sleep(100);
		} catch (IOException e1) {
			System.out.println("Could not send command");
			e1.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Thread interrupted");
			e.printStackTrace();
		}
		closeBluetoothConn();
		System.out.println("Force quit... Reset the brick.");
	}

	private void openBluetoothConn(String robotName) throws IOException {
		comm = null;
		try {
			comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e) {
			System.err.println("Could not create connection: " + e.toString());
		}

		System.out.println("Attempting to connect to robot...");

		try {
			comm.open(nxtInfo);
			outStream = comm.getOutputStream();
			inStream = comm.getInputStream();
		} catch (NXTCommException e) {
			throw new IOException("Failed to connect " + e.toString());
		}
		robotReady = true;
		connected = true;
   }

	private void closeBluetoothConn() {
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

	private int sendToRobot(short[] comm) throws IOException {
		clearBuff();
		if (!connected)
			return -3;
		if (buffer < 2) {
			ByteBuffer b = ByteBuffer.allocate(8);

			for (int i = 0; i < 4; i++) {
				b.putShort(comm[i]);
			}

			byte[] command = b.array();

			outStream.write(command);
			outStream.flush();
			buffer += 1;
		} else {
			// The buffer is full we can't send a package;
			System.out.println("Buffer is full, command not sent!");
			return -1;
		}

		short[] confirmation;
		try {
			confirmation = receiveFromRobot();
			if (confirmation[1] == comm[0]) {
				LCD.drawString("got message", 0, 2);
				System.out.println("Successfully sent message");
				buffer -= 1;
				return confirmation[1];
			} else {
				buffer -= 1;
				return -2;
			}
		} catch (IOException e1) {
			System.out.println("Could not receive confirmation");
			buffer -= 1;
			return -2;
		}

	}

	
	private short[] receiveFromRobot() throws IOException {
		byte[] res = new byte[8];
		inStream.read(res);
		short[] ret = { (short) (res[0] << 8 | (res[1] & 0xFF)),
						(short) (res[2] << 8 | (res[3] & 0xFF)),
						(short) (res[4] << 8 | (res[5] & 0xFF)),
						(short) (res[6] << 8 | (res[7] & 0xFF))
						};
		
		return ret;
	}

	public boolean isConnected() {
		return connected;
	}

	public boolean isRobotReady() {
		return robotReady;
	}

	public void clearBuff() {
		buffer = 0;
	}
	

	private int attemptConnection(short[] command) {

		int confirmation = 0;
		System.out.println("Command: " + command[0]);
		//for (int i = 0; i<10; i++){
			try {
				confirmation = sendToRobot(command);
				
			} catch (IOException e1) {
				System.out.println("Could not send command");
				e1.printStackTrace();
			}
		//}
		System.out.println("Confirmation: " + confirmation);
		return confirmation;
	}
}

