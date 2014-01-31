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

	public static void main(String[] args) throws IOException, InterruptedException {

		String connected = "Connected";
		String waiting = "Waiting...";
		String closing = "Closing...";
		while (true) {
			LCD.drawString(waiting, 0, 0);
			LCD.refresh();

			BTConnection btc = Bluetooth.waitForConnection();

			DataInputStream dis = (DataInputStream) btc.openInputStream();
			DataOutputStream dos = (DataOutputStream) btc.openOutputStream();

			for (int i = 0; i < 100; i++) {
				int n = dis.read();
				LCD.drawInt(n, 7, 0, 1);
				LCD.refresh();
				dos.write(-n);
				dos.flush();
			}

			dis.close();
			dos.close();
			Thread.sleep(100); // wait for data to drain
			LCD.clear();
			LCD.drawString(closing, 0, 0);
			LCD.refresh();
			btc.close();
			LCD.clear();
		}
	}
}
