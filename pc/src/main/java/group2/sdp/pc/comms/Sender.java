//package group2.sdp.pc.comms;
//
//import java.io.*;
//
//import com.sun.corba.se.impl.ior.ByteBuffer;
//
//import lejos.nxt.LCD;
//import lejos.pc.comm.*;
//
///**
// * Calling methods in this class will send messages to device through a stream received in the BTReceive class, and prompts
// * actions in the robot package
// * @author Gordon Edwards
// * @author Michael Mair
// * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843
// * and from SDP Group 4 2013
// */
//public class Sender implements CommInterface {
//	private OutputStream outStream;
//	private InputStream inStream;
//	private NXTComm comm;
//	private boolean connected = false;
//	private int buffer = 0;
//	private NXTInfo nxtInfo;
//	private boolean robotReady;
//
//	public Sender(String robotName, String robotMacAddress) throws IOException{
//		nxtInfo = new NXTInfo(NXTCommFactory.BLUETOOTH, robotName,
//				robotMacAddress);
//		openBluetoothConn(robotName);
//	}
//
//	public int move(int direction, int speed) throws IOException {
//		int[] command = { Commands.ANGLEMOVE, direction, speed, 0};
//		int confirmation = attemptConnection(command);
//		System.out.println("Move...");
//		return confirmation;
//	}
//
//	public int rotate(int angle, int speed) throws IOException {
//		int[] command = { Commands.ROTATE, angle, speed, 0};
//		int confirmation = attemptConnection(command);
//		System.out.println("Rotate...");
//		return confirmation;
//	}
//
//	public int kick(int angle, int speed) throws IOException {
//		int[] command = { Commands.KICK, angle, speed, 0 };
//		int confirmation = attemptConnection(command);
//		System.out.println("Kick");
//		return confirmation;
//
//	}
//
//	public int steer(double turnRate) throws IOException {
//		int[] command = { Commands.STEER, 0, 0, 0 };
//		int confirmation = attemptConnection(command);
//		System.out.println("Steer");
//		return confirmation;
//
//	}
//
//	public int stop() {
//		int[] command = { Commands.STOP, 0, 0, 0 };
//		int confirmation = attemptConnection(command);
//		System.out.println("Stop...");
//		return confirmation;
//	}
//
//	public void disconnect() {
//		int[] command = { Commands.DISCONNECT, 0, 0, 0 };
//		try {
//			sendToRobot(command);
//			// Give the command time to send - prevents brick crash
//			Thread.sleep(100);
//		} catch (IOException e) {
//			System.out.println("Could not send command");
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("Thread interrupted");
//			e.printStackTrace();
//		}
//		closeBluetoothConn();
//		System.out.println("Quit... Please reconnect.");
//	}
//
//	public void forcequit() {
//		int[] command = { Commands.FORCEQUIT, 0, 0, 0 };
//		try {
//			sendToRobot(command);
//			// Give the command time to send - prevents brick crash
//			Thread.sleep(100);
//		} catch (IOException e1) {
//			System.out.println("Could not send command");
//			e1.printStackTrace();
//		} catch (InterruptedException e) {
//			System.out.println("Thread interrupted");
//			e.printStackTrace();
//		}
//		closeBluetoothConn();
//		System.out.println("Force quit... Reset the brick.");
//	}
//
//	private void openBluetoothConn(String robotName) throws IOException {
//
//		comm = null;
//		try {
//			comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
//		} catch (NXTCommException e) {
//			System.err.println("Could not create connection: " + e.toString());
//		}
//
//		System.out.println("Attempting to connect to robot...");
//
//		try {
//			comm.open(nxtInfo);
//			outStream = (OutputStream) comm.getOutputStream();
//			inStream = (InputStream) comm.getInputStream();
//		} catch (NXTCommException e) {
//			throw new IOException("Failed to connect " + e.toString());
//		}
//		robotReady = true;
//		connected = true;
//   }
//
//	private void closeBluetoothConn() {
//		try {
//	    	inStream.close();
//	    	outStream.close();
//	    	comm.close();
//	    	connected = false;
//	   	} catch (IOException ioe) {
//	    	System.out.println("IOException closing connection:");
//	    	System.out.println(ioe.getMessage());
//	    }
//	}
//
//	private int sendToRobot(int[] comm) throws IOException {
//		if (!connected)
//			return -3;
//		if (buffer < 2) {
//			ByteBuffer b = new ByteBuffer(8);
//
//			for (int i = 0; i < 4; i++) {
//				b.append(comm[i]);
//			}
//
//
//			byte[] command = b.toArray();//{ (byte) comm[0], (byte) comm[1], (byte) comm[2],
//					//(byte) comm[3] };
//
//			outStream.write(command);
//			outStream.flush();
//			buffer += 1;
//		} else {
//			// The buffer is full we can't send a package;
//			return -1;
//		}
//
//		int[] confirmation;
//		try {
//			confirmation = receiveFromRobot();
//			if (confirmation[1] == comm[0]) {
//				LCD.drawString("got message", 0, 2);
//				System.out.println("Successfully sent message");
//				buffer -= 1;
//				return confirmation[1];
//			}
//		} catch (IOException e1) {
//			System.out.println("Could not receive confirmation");
//			buffer -= 1;
//			return -2;
//		}
//		System.out.println("Buffer is full, command not sent!");
//		return -2;
//	}
//
//	private int[] receiveFromRobot() throws IOException {
//		byte[] res = new byte[4];
//		inStream.read(res);
//		int[] ret = { (int) res[0], (int) res[1], (int) res[2],
//				(int) res[3] };
//
//		return ret;
//	}
//
//	public boolean isConnected() {
//		return connected;
//	}
//
//	public boolean isRobotReady() {
//		return robotReady;
//	}
//
//	public void clearBuff() {
//
//		buffer = 0;
//	}
//
//	private int attemptConnection(int[] command) {
//		int confirmation = 0;
//
//		for (int i = 0; i<10; i++){
//			try {
//				confirmation = sendToRobot(command);
//				if (confirmation != -1 && confirmation != -2) {
//					break;
//				}
//			} catch (IOException e1) {
//				System.out.println("Could not send command");
//				e1.printStackTrace();
//			}
//		}
//
//		return confirmation;
//	}
//}
//
