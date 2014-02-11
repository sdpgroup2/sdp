/**
 * @author Michael Mair
 * @author Gordon Edwards
 * @author Jaroslaw Hirniak 
 * */

package group2.sdp.pc.ai;

import java.io.IOException;


import group2.sdp.pc.CommandQueue;
import group2.sdp.pc.comms.Commands;
import group2.sdp.pc.comms.Sender;
import group2.sdp.pc.world.Constants;

public class Planner {
	
	private Sender sender;
    
	public Planner (String robotName) {
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
				}	
			}
			
		});
		
		popThread.start();
	}
}
