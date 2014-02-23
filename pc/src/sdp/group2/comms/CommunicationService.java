/**
 * @author Place your name here 
 * */

package sdp.group2.comms;


import java.io.IOException;
import java.util.HashSet;


import sdp.group2.pc.CommandQueue;
import sdp.group2.util.Constants;


public class CommunicationService {
	
private Sender sender;
    
	public CommunicationService (String robotName) {
		try {
			if (robotName.equals(Constants.ROBOT_2A_NAME)) {
				sender = new Sender(Constants.ROBOT_2A_NAME,Constants.ROBOT_2A_MAC);
			} else if(robotName.equals(Constants.ROBOT_2D_NAME)) {
				sender = new Sender(Constants.ROBOT_2D_NAME,Constants.ROBOT_2D_MAC);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void startRunningFromQueue() {
		System.out.println("popping from 2D queue");
		Thread popThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if (!CommandQueue.isEmpty(Constants.ROBOT_2A_NAME)) {
						int[] commands = CommandQueue.poll(Constants.ROBOT_2A_NAME);
						try {
							switch(commands[0]) {
							case Commands.ANGLEMOVE:
								sender.move(commands[1],commands[2],commands[3]);
								break;
							case Commands.ROTATE:
								sender.rotate(commands[1],commands[2]);
								break;
							case Commands.KICK:
								sender.kick(commands[1],commands[2]);
								break;
							case Commands.STEER:
								sender.steer(commands[1]);
								break;
							default:
								
							}
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
					if (!CommandQueue.isEmpty(Constants.ROBOT_2D_NAME)) {
						
						int[] commands = CommandQueue.poll(Constants.ROBOT_2D_NAME);
						try {
							switch(commands[0]) {
							case Commands.ANGLEMOVE:
								sender.move(commands[1],commands[2],commands[3]);
								break;
							case Commands.ROTATE:
								sender.rotate(commands[1],commands[2]);
								break;
							case Commands.KICK:
								sender.kick(commands[1],commands[2]);
								break;
							case Commands.STEER:
								sender.steer(commands[1]);
								break;
							default:
								
							}
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				}	
			}
			
		});
		
		popThread.start();
		Thread attemptClear = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2A_NAME)) {
						sender.stop();
						sender.clearBuff();
						CommandQueue.clear(Constants.ROBOT_2A_NAME);
					} else if (CommandQueue.containsCommand(Commands.clear(), Constants.ROBOT_2D_NAME)) {
						sender.stop();
						sender.clearBuff();
						CommandQueue.clear(Constants.ROBOT_2D_NAME);
					}		
				}
				
			}

		});
		attemptClear.start();

	}
	
}
