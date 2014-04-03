//package sdp.group2.world;
//
//import sdp.group2.communication.CommandQueue;
//import sdp.group2.geometry.Point;
//import sdp.group2.pc.MasterController;
//import sdp.group2.strategy.PID;
//import sdp.group2.util.Constants;
//
//public class RobotPID extends Robot {
//
//	private PID defenceAngle = new PID(90);
//	
//	public RobotPID(Point robotPosition, Point dotPosition, int zone,
//			String name) {
//		super(robotPosition, dotPosition, zone, name);
//	}
//	
//	@Override
//	public void defenceAlign() {
//		double u = defenceAngle.getAdjustment(angleToX());
//		System.out.println(u);
//		
//		System.out.println(CommandQueue.commandQueue2D.size());
//		rotate(u);
//	}
//	
//	@Override
//	public boolean shouldPassAlign() {
//    	double angle = angleToX();
//		double unsignedAngle = Math.abs(angle);
//		
//		// The angle is wrong if it is more than 10 degrees away from 90.
//		return !(unsignedAngle < 6);
//    }
//	
//	@Override
//	public void passAlign() {
//		// Angle ranges from -180 to 180 degrees.
//		double angle = angleToX();	
//		System.out.printf("Rotate by: %f.2\n", angle);
//		System.out.println(CommandQueue.commandQueue2D.size());
//		int sign = MasterController.ourSide == Constants.TeamSide.LEFT ? 1 : -1;
//		rotate(-0.7 * angle * sign); // TODO: Test if works as expected
//	}
//	
//}
