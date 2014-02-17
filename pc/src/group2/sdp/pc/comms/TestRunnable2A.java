package group2.sdp.pc.comms;

import java.io.IOException;

public class TestRunnable2A implements Runnable {

	private static final short KICK_ANGLE = 90;
	private static final short KICK_SPEED = 15234;
	private static final short ROTATE_ANGLE = 90;
	private static final short ROTATE_SPEED = 15234;
	private static final short MOVE_DIRECTION = 1;
	private static final short MOVE_SPEED = 15234;

	@Override
	public void run() {
		Sender sender = null;
		try {
			sender = new Sender("SDP2A","00165307D55F");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		sender.clearBuff();
		try {
			sender.move(1, 4554, 200);
			Thread.sleep(1000);
			sender.clearBuff();
			sender.kick(KICK_ANGLE, KICK_SPEED);
			Thread.sleep(1000);
			sender.move(1, 4554, -200);
			Thread.sleep(1000);
////			connection.clearBuff();
//			connection.rotate(ROTATE_ANGLE, ROTATE_SPEED);
//			connection.clearBuff();
//			Thread.sleep(1000);
//			connection.stop();
//			Thread.sleep(3000);
//			connection.move(MOVE_DIRECTION, MOVE_SPEED);
//			Thread.sleep(1000);
//			connection.clearBuff();
////			connection.clearBuff();
//			connection.rotate(ROTATE_ANGLE, ROTATE_SPEED);
//			connection.clearBuff();
//			Thread.sleep(1000);
//			connection.move(MOVE_DIRECTION, MOVE_SPEED);
//			Thread.sleep(1000);
//
//			connection.clearBuff();
//			connection.kick(KICK_ANGLE, KICK_SPEED);
//			connection.rotate(ROTATE_ANGLE, ROTATE_SPEED);
//			connection.clearBuff();
//			Thread.sleep(1000);
//			connection.move(MOVE_DIRECTION, MOVE_SPEED);
//			Thread.sleep(1000);
//			connection.clearBuff();
//			connection.kick(KICK_ANGLE, KICK_SPEED);
//			Thread.sleep(1000);
			sender.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sender.clearBuff();
			sender.disconnect();
		}

	}


}
