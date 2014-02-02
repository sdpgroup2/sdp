package group2.sdp.pc;

import java.io.IOException;

public class Communication {
	public static void main(String[] args) {
		//note of name and MAC
		BTSend btSendR1 = new BTSend("SDP 2D","0016530BBBEA");
		BTSend btSendR2 = new BTSend("SDP 2A", "00165307D55F");
	}
}
