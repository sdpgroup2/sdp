package sdp.group2.comms;

import sdp.group2.control.Pilot;

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
		System.out.println("Waiting for connection...");
		while (!forceQuit) {
			try {

				NXTConnection connection = Bluetooth.waitForConnection();
				inStream = connection.openInputStream();
				outStream = connection.openOutputStream();
				LCD.clear();
				LCD.drawString("Connected!", 0, 2);

				// Begin reading commands
				short opcode = Commands.DO_NOTHING;
				short option1, option2, option3;

				while ((opcode != Commands.DISCONNECT) && (opcode != Commands.FORCEQUIT) && !(Button.ESCAPE.isDown())) {

					// Get the next command from the inputstream
					byte[] bytes = new byte[8];
					inStream.read(bytes, 0, 8);
					
					short[] command = bytesToCommand(bytes);
					opcode = command[0];
					option1 = command[1];
					option2 = command[2];
					option3 = command[3];

					LCD.clear();
					LCD.refresh();
					LCD.drawString(Commands.getInitial(opcode)+" "+option1+" "+option2+" "+option3, 0, 2);
					boolean replying = true;
					
					switch (opcode) {
					
						case Commands.ANGLEMOVE:
							pilot.move(option1, option2, option3);
							break;
						
						case Commands.ROTATE:
							pilot.rotate(option1, option2);
							break;
	
						case Commands.KICK:
							pilot.kick(option1, option2);
							break;
						
						case Commands.STOP:
							pilot.stop();
							break;
						
						case Commands.STEER:
							pilot.steer(option1);
							break;
							
						case Commands.OPENKICKER:
							pilot.openKicker();
							break;
							
						case Commands.CLOSEKICKER:
							pilot.closeKicker();
							break;
							
						case Commands.ROTATEKICKER:
							pilot.rotateKicker();
							break;
						
						case Commands.DISCONNECT: 
							replying = false;
							break;
							
						case Commands.FORCEQUIT:							
							forceQuit = true;
							replying = false;
							break;
							
						default:
							replying = false;
							break;
					}
					if (replying) {
						replyToPC(opcode, outStream);
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
	
	private static short[] bytesToCommand(byte[] bytes) {
		
		short[] command = new short[4];

		for (int i = 0; i < 4 ; i++) {
			command[i] = (short) (bytes[i*2] << 8 | bytes[i*2 + 1] & 0xFF);
		}

		return command;
		
	}
}
