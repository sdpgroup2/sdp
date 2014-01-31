package group2.sdp.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTReceive {
	/**class checking stream repeatedly for a new message, and respond to it **/
	public static void main(String[] args) throws IOException, InterruptedException {	
		while (true) {

			BTConnection btc = Bluetooth.waitForConnection();
			
			DataInputStream dis = (DataInputStream) btc.openInputStream();
			DataOutputStream dos = (DataOutputStream) btc.openOutputStream();
			String receivedMessage = dis.readUTF();
			
			if (receivedMessage.equals("forward")) {
				//do appropriate action
			} 
			
			dis.close();
			dos.close();
			Thread.sleep(100); // wait for data to drain
			LCD.clear();
//			LCD.drawString(closing, 0, 0);
			LCD.refresh();
			btc.close();
			LCD.clear();
		}
	}
}
