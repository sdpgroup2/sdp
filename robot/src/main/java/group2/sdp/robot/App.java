package group2.sdp.robot;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;


public class App {
	
	private static boolean rotateRight;
	private static boolean rotateLeft;
	private static boolean exit;
	private static double priorDistance;

	public static void main(String[] args) {
		Pilot pilot = new Pilot();
		System.out.print("done");
		rotateRight = false;
		rotateLeft = false;
		exit = false;
		
		while(!exit){
			if((pilot.leftSensor.readValue() > 37 && pilot.rightSensor.readValue() < 37)
					) {
//				pilot.setRotateSpeed(40);
//				pilot.rotateRight();
				rotateRight = true;
				priorDistance = (Motor.A.getTachoCount() + Motor.C.getTachoCount())/2;
				exit = true;
			}
			if(pilot.rightSensor.readValue() > 37 && pilot.leftSensor.readValue() < 37) {
//				pilot.setRotateSpeed(40);
//				pilot.rotateLeft();		Value() > 37 || pilot.leftSensor.readValue() > 37)) {	
				rotateLeft = true;
				priorDistance = (Motor.A.getTachoCount() + Motor.C.getTachoCount())/2;
				exit = true;
//				System.out.println("left");
			}
//			if(pilot.rightSensor.readValue() > 37 && pilot.leftSensor.readValue() > 37) {
////				pilot.arcBackward(45);
////				pilot.setRotateSpeed(55);
////				pilot.rotateRight();
//				rotateRight = true;
//				exit = true;
//			}
			
			if(pilot.leftSensor.readValue() < 37 && pilot.rightSensor.readValue() < 37) {
				pilot.setTravelSpeed(50);
				pilot.forward(); 
			}
			
		}
		boolean stop = false;
		while (exit && !stop) {
			if(rotateRight && (pilot.rightSensor.readValue() > 37 || pilot.leftSensor.readValue() > 37)) {
				// pilot.arcBackward(30);
				pilot.setRotateSpeed(60);
				pilot.rotateRight();
			}
			if(rotateLeft && (pilot.rightSensor.readValue() > 37 || pilot.leftSensor.readValue() > 37)) {
				// pilot.arcBackward(-30);
				pilot.setRotateSpeed(60);
				pilot.rotateLeft();			
			}
			
			if(pilot.leftSensor.readValue() < 37 && pilot.rightSensor.readValue() < 37) {
				pilot.setTravelSpeed(100);
				pilot.forward(); 
			}
			if (((Motor.A.getTachoCount() + Motor.C.getTachoCount())/2)-priorDistance >4900) {
				pilot.stop();
			}
//			LCD.drawInt((int) Math.round((((Motor.A.getTachoCount() + Motor.C.getTachoCount())/2)-priorDistance)),0,0);
		}	
	}
}