package group2.sdp.pc.world;

import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;

import java.util.List;

public class Pitch {

	private PitchLines lines;
	private PitchSection sections;

	private Robot pitchRect;
	private Robot leftAttack;
	private Robot rightAttack;
	private Robot leftDefense;
	private Robot rightDefense;

	private Robot leftDefender = null;
	private Robot leftAttacker = null;
	private Robot rightDefender = null;
	private Robot rightAttacker = null;

	private Ball ball;

	private final VecI LEFT_DEF_POINT = new VecI(120, 240);
	private final VecI LEFT_ATT_POINT = new VecI(270, 240);
	private final VecI RIGHT_DEF_POINT = new VecI(370, 240);
	private final VecI RIGHT_ATT_POINT = new VecI(410, 240);

	public Pitch(PitchLines lines, PitchSection sections) {
		this.lines = lines;
		this.sections = sections;

		pitchRect = getPitchRect();
		leftDefense = getPitchRect(LEFT_DEF_POINT);
		leftAttack = getPitchRect(LEFT_ATT_POINT);
		rightDefense = getPitchRect(RIGHT_DEF_POINT);
		rightAttack = getPitchRect(RIGHT_ATT_POINT);

	}

	public Robot getPitchRect(VecI rectMember) {
		Robot result = new Robot(0, 1, 0, 1);
		List<Robot> rects = sections.getImportantRects();
		for (Robot rect : rects) {
			if (rectMember.x > rect.minX && rectMember.x < rect.maxX && rectMember.y > rect.minY && rectMember.y < rect.maxY) {
				result = rect;
			}
		}

		return result;
	}

	public void addBall(Ball ball) {
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
	public void addRobots(List<Robot> blueRobots, List<Robot> yellowRobots) {
		Robot firstBlueRobot = blueRobots.get(0);
		Robot secondBlueRobot = blueRobots.get(1);
		Robot firstYellowRobot = yellowRobots.get(0);
		Robot secondYellowRobot = yellowRobots.get(1);

		if (this.leftDefense.contains(firstBlueRobot)) {
			this.leftDefender = firstBlueRobot;
			this.leftAttacker = secondBlueRobot;
			if (this.rightDefense.contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.leftAttack.contains(firstBlueRobot)) {
			this.leftAttacker = firstBlueRobot;
			this.leftDefender = secondBlueRobot;
			if (this.rightDefense.contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.rightDefense.contains(firstBlueRobot)) {
			this.rightDefender = firstBlueRobot;
			this.rightAttacker = secondBlueRobot;
			if (this.leftDefense.contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		} else {
			this.rightAttacker = firstBlueRobot;
			this.rightDefender = secondBlueRobot;
			if (this.leftDefense.contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		}
	}

	public Robot getPitchRect() {
		return lines.getImportantRects().get(0);
	}
}
