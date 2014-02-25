package sdp.group2.ai;

import sdp.group2.comms.Commands;
import sdp.group2.comms.Sender;
import sdp.group2.geometry.Line;
import sdp.group2.geometry.Plane;
import sdp.group2.geometry.Point;
import sdp.group2.pc.CommandQueue;
import sdp.group2.util.Constants;
import sdp.group2.world.*;

import java.io.IOException;


public class OffensivePlanner extends Planner {

    private static final Point GOAL = new Point(0, Plane.pix2mm(70));
    private static final int SPEED = 100;
    private static final String robotName = Constants.ROBOT_2A_NAME;
    private Zone offensiveZone;
    private Robot attackerRobot;
    private boolean isRobotAligned = false;
    private Sender sender;

    public OffensivePlanner(IPitch pitch, byte zoneId) {
        super(pitch);
        this.offensiveZone = getPitch().getZone(zoneId);
        this.attackerRobot = pitch.getOurAttacker();
    }

    public boolean isAbleToScore() {
        Point ballPosition = pitch.getBall().getPosition();
        Line arrow = offensiveZone.expand(ballPosition, 0.0);
        Point endpoint = offensiveZone.getIntersection(arrow);
        double space = Math.abs(endpoint.distance(endpoint));

        return space >= attackerRobot.getRadius() * 2;
    }

    public void score() {
        Point ballPosition = pitch.getBall().getPosition();
        Line line1 = getTargetPath();
        // project
        Line line0 = line1.getPerpendicular();
        CommandQueue.add(Commands.kick(Constants.ATK_KICK_ANGLE, Constants.ATK_KICK_POWER), robotName);
    }


    public Line getTargetPath() {
        Ball ball = pitch.getBall();
        Point ballPosition = ball.getPosition();
        Line arrow = offensiveZone.expand(GOAL, ballPosition);
        arrow.extend(attackerRobot.getRadius() + ball.getRadius());
        Line target = arrow.getPerpendicular();
        return offensiveZone.expand(target);
    }

    public void align(double direction) {
        double robotDirection = attackerRobot.getDirection();
        double theta = Math.abs(direction - Math.abs(robotDirection)); // rotation
        // to
        // align
        int sign = direction > robotDirection ? 1 : -1;
        int thetaDeg = sign * Zone.rad2deg(theta);

        try {
            sender.rotate(thetaDeg, SPEED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void act() {
        if (attackerRobot.isMoving()) {
            return;
        }
        if (isAbleToScore()) {
            score();
        } else {
            //Move to position in which isAbleToScore();
        }
    }

}
