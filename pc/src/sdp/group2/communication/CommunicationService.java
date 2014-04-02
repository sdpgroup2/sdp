/**
 * @author Gordon Edwards and Michael Mair
 * */

package sdp.group2.communication;

import java.io.IOException;

import sdp.group2.pc.Timer;
import sdp.group2.util.Constants;

public class CommunicationService {
	
	// In nanoseconds. 40,000,000 is the same delay as the camera feed.
	private static final long A_MILLION = 1000000;
	private static final long COMM_THREAD_DELAY = 20000000;
	
	private Sender sender2A;
	private Sender sender2D;
	
	public CommunicationService() {
		sender2A = connect(Constants.ROBOT_2A_NAME);
		sender2D = connect(Constants.ROBOT_2D_NAME);
	}
	
	public Sender connect(String robotName) {
		String mac = (robotName.equals(Constants.ROBOT_2A_NAME)) ? Constants.ROBOT_2A_MAC : Constants.ROBOT_2D_MAC;
		Sender sender;
		try {
			sender = new Sender(robotName, mac);
			System.out.println("Connected to "+robotName);
			return sender;
		} catch (IOException e) {
			System.err.println("Can't connect to "+robotName);
			return null;
		}
	}
	
	public void startRunningFromQueue() {
		Thread popThread2A = new Thread(robotCommandRunnable(Constants.ROBOT_2A_NAME));
		Thread popThread2D = new Thread(robotCommandRunnable(Constants.ROBOT_2D_NAME));
		popThread2A.start();
		popThread2D.start();
	}
	
	public Runnable robotCommandRunnable(final String robotName) {
		final Sender s = (robotName.equals(Constants.ROBOT_2A_NAME)) ? sender2A : sender2D;
		return new Runnable() {
			@Override
			public void run() {
				while (true) {
					long before = System.nanoTime();
					if (!CommandQueue.isEmpty(robotName)) {
						short[] commands = new short[4];
						int i = 0;
						for (int command : CommandQueue.peek(robotName)) {
							
							commands[i] = (short) command;
							i++;        
						}
						try {
							s.command(commands);
							CommandQueue.poll(robotName);
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
					long delay = System.nanoTime() - before;
					if (delay < COMM_THREAD_DELAY) {
						long sleepTime = (COMM_THREAD_DELAY - delay);
						try {
							Thread.sleep(sleepTime / A_MILLION);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
	}
	
}
