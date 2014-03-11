/**
 * @author Place your name here 
 * */

package sdp.group2.communication;


import java.io.IOException;
import java.util.HashSet;


import sdp.group2.util.Constants;


public class CommunicationService {
	
	private Sender sender2A;
	private Sender sender2D;   
	
	public CommunicationService() {

		try {
			sender2A = new Sender(Constants.ROBOT_2A_NAME,Constants.ROBOT_2A_MAC);
			sender2D = new Sender(Constants.ROBOT_2D_NAME,Constants.ROBOT_2D_MAC);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void startRunningFromQueue() {
		Thread popThread2A = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					sendCommands(sender2A);
				}
			}
			
		});
		
		popThread2A.start();
		
		Thread popThread2D = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					sendCommands(sender2D);
				}
			}
			
		});
		
		popThread2D.start();
		
		Thread attemptClear = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2A_NAME)) {
						sender2A.clearBuff();
						CommandQueue.clear(Constants.ROBOT_2A_NAME);
					} else if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2D_NAME)) {
						sender2D.clearBuff();
						CommandQueue.clear(Constants.ROBOT_2D_NAME);
					}
				}
				
			}

		});
		attemptClear.start();

	}
	
	public void sendCommands(Sender sender) {
		if (!CommandQueue.isEmpty(sender.getName())) {
			short[] commands = new short[4];
			int i = 0;
			for (int command : CommandQueue.poll(sender.getName())) {
				commands[i] = (short) command;
				i++;        
			}
			try {		
				sender.command(commands);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
