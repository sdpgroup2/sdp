package group2.sdp.pc.comms;

import java.io.IOException;

import lejos.nxt.LCD;

/**class to test the communication, make a BTSend instance and call methods to send message to device**/
public class Communication {
	public static void main(String[] args) {
		
		Sender btSendR1 = null;
		Sender btSendR2 = null;
		try {
			//note of name and MAC
//			btSendR1 = new BTSend("SDP 2D","0016530BBBEA");
			btSendR2 = new Sender("SDP 2A", "00165307D55F");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("problem connecting" + e1.getMessage());
		}
//		
//		try {
//			Thread.sleep(1000);
//			btSendR2.kick(90, 10000);
//		} catch (IOException e) {
//			LCD.drawString("problem moving", 10, 10);
//		} catch (InterruptedException e) {
//
//			e.printStackTrace();
//		}
	}
}
