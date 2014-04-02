package sdp.group2.communication;

import java.io.IOException;
import java.util.Scanner;

import sdp.group2.util.Constants;


public class TestComms {

	public static void main(String[] args) {

		CommunicationService commService = new CommunicationService();
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		commService.startRunningFromQueue();

//		for (int i = 0; i<4; i++){
//			System.out.println("close kick");
//			CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2A_NAME);
//			System.out.println("open kick");
//			CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2A_NAME);		
//		}
//		System.out.println("MOVE");
//		CommandQueue.add(Commands.move(1, 3433, 500), Constants.ROBOT_2A_NAME);	
//		System.out.println("rotate");
//		CommandQueue.add(Commands.rotate(90,200), Constants.ROBOT_2A_NAME);
//		CommandQueue.add(Commands.closeKicker(),Constants.ROBOT_2A_NAME);
//		CommandQueue.add(Commands.disconnect(),Constants.ROBOT_2A_NAME);
		Scanner scanner = new Scanner(System.in);
//		System.out.println("Type more than 10 characters to quit.");
		String line = scanner.nextLine();
		CommandQueue.add(Commands.kick(0,1000), Constants.ROBOT_2D_NAME);
//		boolean added;
//		while (true) {
////			System.out.println("lololololo");
//			added = CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2D_NAME);
//			if (added) {
//				System.out.println("Opened");
//			}
//			added = CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2D_NAME);
//			if (added) {
//				System.out.println("Closed");
//			}
//		}
		
		while (line.length() < 10) {
//			CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2A_NAME);
//			line = scanner.nextLine();
//			CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2A_NAME);
//			line = scanner.nextLine();
//			CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2D_NAME);
//			line = scanner.nextLine();
//			CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2D_NAME);
//			try {
//				Thread.sleep(7);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			CommandQueue.add(Commands.move(1, 200,100), Constants.ROBOT_2A_NAME);
			 
			line = scanner.nextLine(); 
//			CommandQueue.add(Commands.rotate(34,456), Constants.ROBOT_2A_NAME);
//			line = scanner.nextLine();
//			CommandQueue.add(Commands.rotate(-76,4456), Constants.ROBOT_2A_NAME);
		}
		
//		CommandQueue.add(Commands.rotate(90, 67), Constants.ROBOT_2A_NAME);
	}

}
