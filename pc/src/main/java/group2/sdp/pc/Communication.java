package group2.sdp.pc;

import java.io.IOException;

public class Communication {
	public static void main(String[] args) {
		try {
			BTSend.sendForwardMessage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
