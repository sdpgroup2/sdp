package sdp.group2.communication;

import java.util.Scanner;

import sdp.group2.util.Constants;


public class TestComms {

	public static void main(String[] args) {

		CommunicationService commService = new CommunicationService();
		commService.startRunningFromQueue();

//		for (int i = 0; i<4; i++){
//			System.out.println("close kick");
//			CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2D_NAME);
//			System.out.println("open kick");
//			CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2D_NAME);		
//		}
//		System.out.println("MOVE");
//		CommandQueue.add(Commands.move(1, 3433, 500), Constants.ROBOT_2D_NAME);	
//		System.out.println("rotate");
//		CommandQueue.add(Commands.rotate(90,200), Constants.ROBOT_2D_NAME);
		Scanner scanner = new Scanner(System.in);
		System.out.println("Type more than 10 characters to quit.");
		String line = scanner.nextLine();
		while (line.length() < 10) {
			CommandQueue.add(Commands.closeKicker(), Constants.ROBOT_2D_NAME);
			line = scanner.nextLine();
			CommandQueue.add(Commands.openKicker(), Constants.ROBOT_2D_NAME);
			line = scanner.nextLine();
		}
		CommandQueue.add(Commands.disconnect(), Constants.ROBOT_2D_NAME);
	}

}
