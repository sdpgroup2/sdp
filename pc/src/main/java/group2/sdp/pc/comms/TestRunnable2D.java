//package group2.sdp.pc.comms;
//
//import java.io.IOException;
//
//public class TestRunnable2D implements Runnable {
//
//	private static final short KICK_ANGLE = 90;
//	private static final short KICK_SPEED = 1234;
//	private static final short ROTATE_ANGLE = 90;
//	private static final short ROTATE_SPEED = 1234;
//	private static final int MOVE_DIRECTION = 1;
//	private static final int MOVE_SPEED = 1234;
//
//	@Override
//	public void run() {
//		Sender connection = null;
//		try {
//			connection = new Sender("SDP 2D","0016530BBBEA");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		connection.clearBuff();
//		try {
//			connection.clearBuff();
//			connection.move(MOVE_DIRECTION, MOVE_SPEED);
//			Thread.sleep(1000);
//			connection.rotate(ROTATE_ANGLE, ROTATE_SPEED);
//			Thread.sleep(1000);
//			connection.clearBuff();
//			connection.kick(MOVE_DIRECTION, MOVE_SPEED);
//			Thread.sleep(1000);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			connection.disconnect();
//		}
//
//	}
//
//
//}
