package group2.sdp.robot;


public class App {
	
	public static void main(String[] args) {
		Pilot pilot = new Pilot();
		while(true){
		while(pilot.leftSensor.readValue() > 37 && pilot. rightSensor.readValue() < 37) {
			pilot.setRotateSpeed(75);
			pilot.rotateRight();
		}
		while(pilot.rightSensor.readValue() > 37 && pilot.leftSensor.readValue() < 37) {
			pilot.setRotateSpeed(75);
			pilot.rotateLeft();			
		}
		while(pilot.rightSensor.readValue() > 37 && pilot.leftSensor.readValue() > 37) {
			pilot.arcBackward(45);
			pilot.setRotateSpeed(75);
			pilot.rotateRight();
		}
		
		while(pilot.leftSensor.readValue() < 37 && pilot.rightSensor.readValue() < 37) {
			pilot.forward(); 
		}
			
		}
			
		
	}
}
