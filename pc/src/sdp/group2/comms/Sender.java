package sdp.group2.comms;

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
public class Sender implements CommInterface {
	private OutputStream outStream;
	private InputStream inStream;
	private NXTComm comm;
	private boolean connected = false;
	private int buffer = 0;
	private NXTInfo nxtInfo;
	private boolean robotReady;
	private boolean lock;

	public Sender(String robotName, String robotMacAddress) throws IOException{
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, robotName,
				robotMacAddress);
		openBluetoothConn(robotName);
	}

	public int move(int direction, int speed, int distance) throws IOException {
		int confirmation =0;
		short[] command = { Commands.ANGLEMOVE, (short) direction, (short) speed, (short) distance};
		confirmation = attemptConnection(command);
		System.out.printf("Moving in %d direction with %d speed and %d distance\n", direction, speed, distance);
		return confirmation;
	} 

	public int rotate(int angle, int speed) throws IOException {
		short[] command = { Commands.ROTATE, (short) angle, (short) speed, 0};
		int confirmation = attemptConnection(command);
		System.out.printf("Rotating at a %d angle\n", angle);
		return confirmation;
	}

	public int kick(int angle, int speed) throws IOException {
		short[] command = { Commands.KICK, (short) angle, (short) speed, 0 };
		long timeStart = System.currentTimeMillis();
		int confirmation = attemptConnection(command);
		long timeEnd = System.currentTimeMillis();
		System.out.printf("Kick with angle %d and speed %d, took %dms\n", angle, speed, timeEnd-timeStart);
		return confirmation;

	}
	
	public int steer(int turnRate) throws IOException {
		short[] command = { Commands.STEER, (short) turnRate, 0, 0 };
		int confirmation = attemptConnection(command);
		System.out.println("Steer...");
		return confirmation;

	}

	public int stop() {
		short[] command = { Commands.STOP, 0, 0, 0 };
		int confirmation = attemptConnection(command);
		System.out.println("Stop");
		return confirmation;
	}

	@Override
	public void disconnect() {
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

	@Override
	public void forcequit() {
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
				System.out.printf("Con: [%d %d %d %d], Comm: [%d %d %d %d]\n", confirmation[0], confirmation[1], confirmation[2], confirmation[3], comm[0], comm[1], comm[2], comm[3]);
				System.out.printf("Confirmation should be %d, was %d\n", comm[0], confirmation[1]);
				//System.out.println("Could not receive confirmation");
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
	
	public void lock() {
		this.lock = true;
	}

	private int attemptConnection(short[] command) {
		if (this.lock) {
			return 7;
		}
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
