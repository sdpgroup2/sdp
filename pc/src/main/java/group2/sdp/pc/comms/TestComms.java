package group2.sdp.pc.comms;

import group2.sdp.pc.CommandQueue;
import group2.sdp.pc.ai.Planner;
import group2.sdp.pc.world.Constants;


public class TestComms {

	public static void main(String[] args) {
		
		Planner planner2A = new Planner(Constants.ROBOT_2A_NAME);
//		Planner planner2D = new Planner(Constants.ROBOT_2D_NAME);
		planner2A.startRunningFromQueue();
//		planner2D.startRunningFromQueue();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CommandQueue.add(Commands.move(1, 34, 56), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.kick(30, 3403), Constants.ROBOT_2A_NAME);
		CommandQueue.add(Commands.rotate(34,2344), Constants.ROBOT_2A_NAME);
	}

}
