package group2.sdp.robot.milestones;

import lejos.nxt.LCD;
import lejos.util.TextMenu;
import group2.sdp.robot.Pilot;

public class Milestone2 {
	
	public static final int ATTACKER_KICK_SPEED = 0;
	public static final int DEFENDER_KICK_SPEED = 0;
	
//	public static void main(String[] args) {
//		int kick_speed;
//		
//		// Menu which allows selecting the position
//		
//		String[] menuItems = { "Attack", "Defence" };
//		TextMenu textMenu = new TextMenu(menuItems, 1, "Select Position:");
//		kick_speed = textMenu.select() == 0 ? ATTACKER_KICK_SPEED : DEFENDER_KICK_SPEED;
//		LCD.clear();
//		
//		Pilot pilot = new Pilot();
//		pilot.kick(90, kick_speed);
//	}
	public static void main(String[] args) {
		int kick_speed;
		
		// Menu which allows selecting the position
		
		String[] menuItems = { "100", "150", "180", "200", "400", "500", "1000", "5000", "10000", "100000" };
		TextMenu textMenu = new TextMenu(menuItems, 1, "Select Speed:");
		kick_speed = Integer.parseInt(menuItems[textMenu.select()]);
		LCD.clear();
		
		Pilot pilot = new Pilot();
		pilot.kick(-90, kick_speed);
	}
}
