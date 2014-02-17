/**
 * @author Place your name here 
 * */

package group2.sdp.pc.ai;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import group2.sdp.pc.comms.Commands;
import group2.sdp.pc.comms.Sender;

public class BluetoothWorldCommanderServiceAdapterAbstractionFactoryNyan {
	
	private Sender sender;
    private Queue<int[]> commandQueue;
    
	public BluetoothWorldCommanderServiceAdapterAbstractionFactoryNyan (final Sender sender) {
		this.sender = sender;
		commandQueue = new SynchronousQueue<int[]>();
		
		Thread pushThread = new Thread(new Runnable() {

			@Override
			public void run() {
				
			}
			
		});
		Thread popThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					if (!commandQueue.isEmpty()) {
						int[] commands = commandQueue.poll();
						try {
							switch(commands[0]) {
							case Commands.ANGLEMOVE:
								sender.move(commands[1],commands[2],commands[3]);
							case Commands.ROTATE:
								sender.rotate(commands[1],commands[2]);
							case Commands.KICK:
								sender.kick(commands[1],commands[2]);
							case Commands.STEER:
								sender.steer(commands[1]);
							case Commands.STOP:
								sender.stop();
								
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
	public void addToCommandQueue(int[] command) {
		commandQueue.add(command);
	}
	public static void main(String[] args) {
		
//		int[] strat = new int[] {};
		
		
	}
	/*private int[] move(int direction, int speed, int distance) {
		return new int[] {Commands.ANGLEMOVE, direction,speed,distance};
	}*/
	
}
