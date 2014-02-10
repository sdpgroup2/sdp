package group2.sdp.pc.comms;

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

	public Sender(String robotName, String robotMacAddress) throws IOException{
		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, robotName,
				robotMacAddress);
		openBluetoothConn(robotName);
	}

	@Override
	public int move(short direction, short speed) throws IOException {
		short[] command = { Commands.ANGLEMOVE, direction, speed, 0};
		int confirmation = attemptConnection(command);
		System.out.println("Move...");
		return confirmation;
	}

	@Override
	public int rotate(short angle, short speed) throws IOException {
		short[] command = { Commands.ROTATE, angle, speed, 0};
		int confirmation = attemptConnection(command);
		System.out.println("Rotate...");
		return confirmation;
	}

	@Override
	public int kick(short angle, short speed) throws IOException {
		short[] command = { Commands.KICK, angle, speed, 0 };
		long timeStart = System.currentTimeMillis();
		int confirmation = attemptConnection(command);
		long timeEnd = System.currentTimeMillis();
		System.out.printf("Kick with angle %d and speed %d, took %dms\n", angle, speed, timeEnd-timeStart);
		return confirmation;

	}

	@Override
	public int steer(short turnRate) throws IOException {
		short[] command = { Commands.STEER, 0, 0, 0 };
		int confirmation = attemptConnection(command);
		System.out.println("Steer...");
		return confirmation;

	}

	@Override
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


			byte[] command = b.array();//{ (byte) comm[0], (byte) comm[1], (byte) comm[2],
					//(byte) comm[3] };

			short[] commands = new short[4];

			for (int i = 0; i < 4 ; i++) {
				//commands[i] = b.getShort(i);
				commands[i] = (short) (command[i*2] << 8 | command[i*2 + 1] & 0xFF);
			}

			System.out.printf("Option 1: %d, Option 2: %d, Option 3: %d\n", commands[1], commands[2], commands[3]);

			outStream.write(command);
			outStream.flush();
			buffer += 1;
		} else {
			// The buffer is full we can't send a package;
			System.out.println("Buffer is full, command not sent!");
			return -1;
		}

		int[] confirmation;
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

	private int[] receiveFromRobot() throws IOException {
		byte[] res = new byte[4];
		inStream.read(res);
		int[] ret = { res[0], res[1], res[2],
				res[3] };

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

		//for (int i = 0; i<10; i++){
			try {
				confirmation = sendToRobot(command);
				if (confirmation != -1 && confirmation != -2) {
					//break;
				}
			} catch (IOException e1) {
				System.out.println("Could not send command");
				e1.printStackTrace();
			}
		//}

		return confirmation;
	}
}

