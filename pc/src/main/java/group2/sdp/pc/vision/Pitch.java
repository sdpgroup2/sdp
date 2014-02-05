package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;

import java.util.List;

public class Pitch {

	private PitchLines lines;
	private final PitchSection leftAttack;
	private final PitchSection rightAttack;
	private final PitchSection leftDefense;
	private final PitchSection rightDefense;

	private Rect leftDefender = null;
	private Rect leftAttacker = null;
	private Rect rightDefender = null;
	private Rect rightAttacker = null;

	private Rect ball;

	public Pitch(PitchSection leftAttack, PitchSection rightAttack, PitchSection leftDefense, PitchSection rightDefense) {
		this.leftAttack = leftAttack;
		this.rightAttack = rightAttack;
		this.leftDefense = leftDefense;
		this.rightDefense = rightDefense;
	}

	public void addBall(Rect ball) {
		this.ball = ball;
	}

	/**
	 * Adds the robot to the pitch model, depending on the location of their
	 * rectangle.
	 * 
	 * @param blueRobots
	 *            - List of rectangles of blue robots.
	 * @param yellowRobots
	 *            - List of rectangles of yellow robots.
	 */
	public void addRobots(List<Rect> blueRobots, List<Rect> yellowRobots) {
		Rect firstBlueRobot = blueRobots.get(0);
		Rect secondBlueRobot = blueRobots.get(1);
		Rect firstYellowRobot = yellowRobots.get(0);
		Rect secondYellowRobot = yellowRobots.get(1);

		if (this.leftDefense.getImportantRects().contains(firstBlueRobot)) {
			this.leftDefender = firstBlueRobot;
			this.leftAttacker = secondBlueRobot;
			if (this.rightDefense.getImportantRects().contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.leftAttack.getImportantRects().contains(firstBlueRobot)) {
			this.leftAttacker = firstBlueRobot;
			this.leftDefender = secondBlueRobot;
			if (this.rightDefense.getImportantRects().contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.rightDefense.getImportantRects().contains(firstBlueRobot)) {
			this.rightDefender = firstBlueRobot;
			this.rightAttacker = secondBlueRobot;
			if (this.leftDefense.getImportantRects().contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		} else {
			this.rightAttacker = firstBlueRobot;
			this.rightDefender = secondBlueRobot;
			if (this.leftDefense.getImportantRects().contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		}
	}

	public Rect getPitchRect() {
		return lines.getImportantRects().get(0);
	}
}
