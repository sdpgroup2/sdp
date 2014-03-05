package sdp.group2.strategy;

import sdp.group2.communication.CommandQueue;
import sdp.group2.communication.Commands;
import sdp.group2.communication.Sender;
import sdp.group2.geometry.Millimeter;
import sdp.group2.geometry.Point;
import sdp.group2.util.Constants;
import sdp.group2.world.Ball;
import sdp.group2.world.IPitch;
import sdp.group2.world.Robot;
import sdp.group2.world.Zone;

public class SimpleOffensivePlanner extends Planner{

	private static final int SPEED = 40;
	private static final String robotName = Constants.ROBOT_2A_NAME;
	private static final Point EnemyGoal = new Point(553, Millimeter.pix2mm(153));
	private boolean isRobotAligned = false;
	private Sender sender;
	private long lastRotation = System.currentTimeMillis();
	
	
	public SimpleOffensivePlanner(IPitch pitch){
		super(pitch);
	}
	
	public void goToBallShoot(){
		Robot robot = pitch.getOurAttacker();
		Ball ball = pitch.getBall();
		System.out.print("Going to ball");
		
		int distance = (int) Math.sqrt(Math.pow((ball.getPosition().y - robot.getPosition().y), 2) + Math.pow((ball.getPosition().x - robot.getPosition().x), 2));
		int sign = distance < 0 ? -1 : 1;
		distance = Math.abs(distance);
		
		//Unsure if correct;
		double angleToBall = 90.0 - robot.getDirection();
		//TODO
		double angleToGoal = 0;
		
		System.out.println("Rotating Angle: " + angleToBall);
		System.out.println("Moving Forward: " + distance);
		
		if (Math.abs(angleToBall) > 10.0){
			CommandQueue.add(Commands.rotate(( (int) Math.floor(angleToBall)), Constants.ATK_MOVE_SPEED), robotName);
			
		} else if (distance < 10) {
			//If beside the ball, attempt to close kicker over it, may(will) need some tweaking
			//Turn to face goal and shoot
			CommandQueue.add(Commands.closeKicker(), robotName);
			CommandQueue.add(Commands.rotate( (int) angleToGoal, Constants.ATK_MOVE_SPEED), robotName);
			CommandQueue.add(Commands.kick(Constants.ATK_KICK_ANGLE, Constants.ATK_KICK_POWER), robotName);
			
		}	else {
			CommandQueue.add(Commands.move(sign, Constants.ATK_MOVE_SPEED, distance), robotName);
		}
		
	}
	
	public void disconnect(){
		this.sender.disconnect();
	}
	
	/**
	 * If ball in our zone, goToBallShoot, else do nothing
	 */
	public void act(){
		if (pitch.getBallZone() == pitch.getOurAttackZone()){
			goToBallShoot();
		}
	}
	
}
