package sdp.group2.comms;

import java.util.Scanner;

import sdp.group2.pc.CommandQueue;
import sdp.group2.ai.Planner;
import sdp.group2.world.Constants;


public class TestComms {

	public static void main(String[] args) {
		
//		Planner planner2D = new Planner(Constants.ROBOT_2D_NAME);
//		Planner planner2D = new Planner(Constants.ROBOT_2D_NAME);
//		planner2D.startRunningFromQueue();
//		planner2D.startRunningFromQueue();

		CommunicationService commService = new CommunicationService(Constants.ROBOT_2A_NAME);
		commService.startRunningFromQueue();

		CommandQueue.add(Commands.move(1, 34, 2000), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.kick(30, 3403), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.rotate(180,2344), Constants.ROBOT_2A_NAME);
		Thread input = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					System.out.println("got here");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				CommandQueue.clear(Constants.ROBOT_2A_NAME);
			}	
		});
		input.start();
	}

}
