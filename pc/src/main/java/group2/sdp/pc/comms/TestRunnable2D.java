package group2.sdp.pc.comms;

import java.io.IOException;

public class TestRunnable2D implements Runnable{

	public void run() {
		Sender connection = null;
		try {
			connection = new Sender("SDP 2D","0016530BBBEA");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		connection.clearBuff();
		try {
			connection.clearBuff();
			connection.move(1, 34, 45667);
			Thread.sleep(5000);
			connection.clearBuff();
			connection.rotate(1, 34, 45667);

			Thread.sleep(4000);
			connection.clearBuff();
			connection.kick(34, 456);
			connection.clearBuff();
			Thread.sleep(2000);
			connection.forcequit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
