package group2.sdp.robot.comms;

import group2.sdp.robot.Pilot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * class checking stream repeatedly for a new message, and respond to it
 * 
 * @author Gordon Edwards and Michael Mair code based on SDP Group 4 2013 and
 *         from burti (Lawrie Griffiths) at
 *         /www.lejos.org/forum/viewtopic.php?p=10843
 **/
public class Receiver {

	private static InputStream inStream;
	private static OutputStream outStream;
	private static boolean forceQuit;
	private static Pilot pilot;
	public static void main(String[] args) throws IOException, InterruptedException {	
		 pilot = new Pilot();
		
		while (!forceQuit) {
			try {

				NXTConnection connection = Bluetooth.waitForConnection();
				inStream = connection.openInputStream();
				outStream = connection.openOutputStream();
				LCD.clear();
				LCD.drawString("Connected!", 0, 2);
				byte[] robotReady = { 0, 0, 0, 0 };
				if (outStream == null)
					throw new Exception("Output stream is null!");
				outStream.write(robotReady);
				outStream.flush();

				// Begin reading commands
				int opcode = Commands.DO_NOTHING;
				int option1, option2, option3;

				while ((opcode != Commands.DISCONNECT) && (opcode != Commands.FORCEQUIT) && !(Button.ESCAPE.isDown())) {

					// Get the next command from the inputstream
					byte[] byteBuffer = new byte[8];
					inStream.read(byteBuffer);
					
					opcode = byteBuffer[0];
					int[] options = bytesToOptions(byteBuffer);
					option1 = options[0];
					option2 = options[1];
					option3 = options[2];

					switch (opcode) {
					
						case Commands.ANGLEMOVE:
							LCD.clear();
//							LCD.drawString("Moving at an angle!", 0, 2);
							LCD.refresh();
							option1 = byteBuffer[1] << 8 | byteBuffer[2];
							option2 = byteBuffer[3] << 8 | byteBuffer[4];
							option3 = byteBuffer[5] << 8 | byteBuffer[6];
							LCD.drawString(option1 + " "+ option2 + " " + option3, 0, 2);
							pilot.move(option1, option2);
							replyToPC(opcode, outStream);
							break;
						
						case Commands.ROTATE:
							LCD.clear();
							LCD.drawString("Rotate!", 0, 2);
							LCD.refresh();
							option1 = byteBuffer[1];
							option2 = byteBuffer[2];
							option3 = byteBuffer[3];
							pilot.rotate(option1, option2);
							replyToPC(opcode, outStream);
							break;
	
						case Commands.KICK:
							LCD.clear();
							LCD.drawString("Kicking!", 0, 2);
							LCD.refresh();
							option1 = byteBuffer[1];
							option2 = byteBuffer[2];
							pilot.kick(option1,option2);
							replyToPC(opcode, outStream);
							break;
						
						case Commands.STOP:
							LCD.clear();
							LCD.drawString("Stopping!", 0, 2);
							LCD.refresh();
							pilot.stop();
							replyToPC(opcode, outStream);
							break;
						
						case Commands.STEER:
							LCD.clear();
							LCD.drawString("Stopping!", 0, 2);
							LCD.refresh();
							pilot.steer(option1);
							replyToPC(opcode, outStream);
							break;
							
						case Commands.DISCONNECT: 
							
							break;
	
						case Commands.FORCEQUIT:
							
							forceQuit = true;
							break;
						default:
							// Ignore it
					}
				}

				// Closing streams and connection
				inStream.close();
				outStream.close();
				Thread.sleep(100); // Waiting for data to drain
				LCD.clear();
				LCD.drawString("Closing", 0, 2);
				LCD.refresh();
				connection.close();
				LCD.clear();
				Thread.sleep(200);
			} catch (Exception e) {
				LCD.drawString("Exception:", 0, 2);
				String msg = e.getMessage();
				if (msg != null) {
					LCD.drawString(msg, 2, 3);
				} else {
					LCD.drawString("Error message is null", 2, 3);
				}
			}
		}
	}

	public static void replyToPC(int opcode, OutputStream os) throws IOException {
		byte[] reply = { 111, (byte) opcode, 0, 0 };
		os.write(reply);
		os.flush();
	}
	
	private static int[] bytesToOptions(byte[] bytes, int[] pattern) {
		
		int[] options = new int[pattern.length];
		int byteNum = 1;
		for (int i = 0; i < pattern.length; i++) {
			for (int j = pattern[i]; j > 0; j--) {
				options[i] = bytes[byteNum];
			}
		}
		options[0] = bytes[1] << 8 | bytes[2];
		options[1] = bytes[3] << 8 | bytes[4];
		options[2] = bytes[5] << 8 | bytes[6];
		return options;
		
	}
}
