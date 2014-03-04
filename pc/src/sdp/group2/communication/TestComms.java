package sdp.group2.communication;

import java.io.IOException;

import sdp.group2.util.Constants;


public class TestComms {

	public static void main(String[] args) {
		CommunicationService commService = new CommunicationService(Constants.ROBOT_2A_NAME);
		commService.startRunningFromQueue();

		CommandQueue.add(Commands.kick(30, 3403), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.kick(30, 3403), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.kick(30, 3403), Constants.ROBOT_2A_NAME);		
		
		Thread input = new Thread(new Runnable() {
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
	}

}
