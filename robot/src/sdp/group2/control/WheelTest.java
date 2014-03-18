package sdp.group2.control;

public class WheelTest {
	
	public static void main(String[] args) {
		Pilot pilot = new Pilot();
//		pilot.moveForward(300);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		pilot.setRotateSpeed(50);
		pilot.rotateRight();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
