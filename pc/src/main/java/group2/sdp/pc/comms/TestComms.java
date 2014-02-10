package group2.sdp.pc.comms;

import java.io.IOException;

public class TestComms {

	public static void main(String[] args) {
		//System.out.print("asfjas");
		Thread robot1Thread = new Thread(new TestRunnable2A());
//		Thread robot2Thread = new Thread(new TestRunnable2D());
		robot1Thread.start();
//		robot2Thread.start();
	}

}
