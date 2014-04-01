/**
 * @author Gordon Edwards and Michael Mair
 * */

package sdp.group2.communication;

import java.io.IOException;
import sdp.group2.util.Constants;

public class CommunicationService {
	
	private Sender sender2A;
	private Sender sender2D;   
	
	public CommunicationService() {
		Thread connect2A = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sender2A = new Sender(Constants.ROBOT_2A_NAME,Constants.ROBOT_2A_MAC);
					System.out.println("Connected to 2A");
				} catch (IOException e) {
					System.err.println("Can't connect to 2A!");
				}
				
			}
			
		});
		Thread connect2D = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					sender2D = new Sender(Constants.ROBOT_2D_NAME,Constants.ROBOT_2D_MAC);
					System.out.println("Connected to 2D");
				} catch (IOException e) {
					System.err.println("Can't connect to 2D!");
				}
			}
			
		});
		connect2A.start();
		connect2D.start();
		try {
			connect2A.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			connect2D.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	public void startRunningFromQueue() {
		Thread popThread2A = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					while(true) {
						if (!CommandQueue.isEmpty(Constants.ROBOT_2A_NAME)) {
							short[] commands = new short[4];
							int i = 0;
							for (int command : CommandQueue.poll(Constants.ROBOT_2A_NAME)) {
								
								commands[i] = (short) command;
								i++;        
							}
							try {	
								
								sender2A.command(commands);
							} catch(IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			
		});
		
		
		
		Thread popThread2D = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if (!CommandQueue.isEmpty(Constants.ROBOT_2D_NAME)) {
						short[] commands = new short[4];
						int i = 0;
						for (int command : CommandQueue.poll(Constants.ROBOT_2D_NAME)) {
							
							commands[i] = (short) command;
							i++;        
						}
						try {	
							
							sender2D.command(commands);
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		popThread2A.start();
		popThread2D.start();
		
		Thread attemptClear = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2A_NAME)) {
						if (sender2A != null) {
							sender2A.clearBuff();
							CommandQueue.clear(Constants.ROBOT_2A_NAME);
						}
					} else if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2D_NAME)) {
						if (sender2D != null) {
							sender2D.clearBuff();
							CommandQueue.clear(Constants.ROBOT_2D_NAME);
						}
					}
				}
				
			}

		});
		attemptClear.start();

	}
	
}
