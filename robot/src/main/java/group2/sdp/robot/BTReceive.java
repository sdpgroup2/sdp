package group2.sdp.robot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class BTReceive {
	/**class checking stream repeatedly for a new message, and respond to it
	 * @author Gordon Edwards and Michael Mair
	 * code based on that from burti (Lawrie Griffiths) at /www.lejos.org/forum/viewtopic.php?p=10843 **/
	
	public static void main(String[] args) throws IOException, InterruptedException {	
		while (true) {

			RemoteDevice remote = Bluetooth.getKnownDevice("SDP 2D");
			BTConnection btc = Bluetooth.connect(remote);
			InputStream dis = (InputStream) btc.openInputStream();
			OutputStream dos = (OutputStream) btc.openOutputStream();
			int receivedMessage = dis.read();
			
			if (receivedMessage == 0) {
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
