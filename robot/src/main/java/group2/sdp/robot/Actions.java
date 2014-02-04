package group2.sdp.robot;

import java.io.*;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;


/**
 * edit this to 
 * @author Gordon Edwards
 *
 */
public class Actions {   
	
	public static void move(int direction, int angle, int speed) {
		LCD.drawString("moving forward!", 10, 10);
		DifferentialPilot dp = new DifferentialPilot(56, 98, Motor.A, Motor.C, true);
		dp.forward();
		dp.setTravelSpeed(speed);
	} 
	
	public static void rotate(int direction, int angle, int speed) {
		LCD.drawString("rotating!", 10, 10);
		DifferentialPilot dp = new DifferentialPilot(56, 98, Motor.A, Motor.C, true);
		dp.setRotateSpeed(speed);
		if (direction == 1) {
			dp.rotate(angle);
		}
		else {
			dp.rotate(-angle);
		}
	}
	
	public static void kick(int angle, int speed) {
		LCD.drawString("kicking!", 10, 10);
		Motor.B.setSpeed(speed);
		Motor.B.rotate(angle);
		Motor.B.rotate(-angle);
		
	}
	
	public static void stop() {

	}
	
	public static void disconnect() {
		
	}

	public static void forcequit() {
		
	}
	

}
