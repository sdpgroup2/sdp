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
					byte[] byteBuffer = new byte[4];
					inStream.read(byteBuffer);

					opcode = byteBuffer[0];
					option1 = byteBuffer[1];
					option2 = byteBuffer[2];
					option3 = byteBuffer[3];

					switch (opcode) {
					
						case Commands.ANGLEMOVE:
							LCD.clear();
							LCD.drawString("Moving at an angle!", 0, 2);
							LCD.refresh();
							pilot.move(option1, option2, option3);
							replyToPC(opcode, outStream);
							break;
						
						case Commands.ROTATE:
							LCD.clear();
							LCD.drawString("Rotate!", 0, 2);
							LCD.refresh();
							pilot.rotate(option1, option2, option3);
							replyToPC(opcode, outStream);
							break;
	
						case Commands.KICK:
							LCD.clear();
							LCD.drawString("Kicking!", 0, 2);
							LCD.refresh();
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
}
