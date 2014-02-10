package group2.sdp.pc.world;

import group2.sdp.pc.geom.Point;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;
import group2.sdp.pc.world.Constants.TeamColor;
import group2.sdp.util.CoordinateTranslator;

import java.util.List;

public class Pitch {

	public Rect getPitchRect() {
		return pitchRect;
	}

	public void setPitchRect(Rect pitchRect) {
		this.pitchRect = pitchRect;
	}

	private PitchLines lines;
	private PitchSection sections;

	private Rect pitchRect;
	private Rect leftAttackZone;
	private Rect rightAttackZone;
	private Rect leftDefenseZone;
	private Rect rightDefenseZone;

	private Robot leftDefender;
	private Robot leftAttacker;
	private Robot rightDefender;
	private Robot rightAttacker;

	private Ball ball;

	// Points that should definitely belong to respective zones
	private final Point LEFT_DEF_POINT = new Point(120, 240);
	private final Point LEFT_ATT_POINT = new Point(270, 240);
	private final Point RIGHT_DEF_POINT = new Point(370, 240);
	private final Point RIGHT_ATT_POINT = new Point(410, 240);

	public Pitch(PitchLines lines, PitchSection sections) {
		this.lines = lines;
		this.sections = sections;

		pitchRect = lines.getImportantRects().get(0);
		leftDefenseZone = getPitchRect(LEFT_DEF_POINT);
		leftAttackZone = getPitchRect(LEFT_ATT_POINT);
		rightDefenseZone = getPitchRect(RIGHT_DEF_POINT);
		rightAttackZone = getPitchRect(RIGHT_ATT_POINT);
	}

	public void addBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 * Adds the robot to the pitch model, depending on the location of their
	 * rectangle.
	 *
	 * @param blueRobots - List of rectangles of blue robots.
	 * @param yellowRobots - List of rectangles of yellow robots.
	 */
	public void addRobots(List<Robot> blueRobots, List<Robot> yellowRobots, TeamColor ourTeam) {
		Robot firstBlueRobot = blueRobots.get(0);
		Robot secondBlueRobot = blueRobots.get(1);
		Robot firstYellowRobot = yellowRobots.get(0);
		Robot secondYellowRobot = yellowRobots.get(1);

		if (this.leftDefenseZone.contains(firstBlueRobot.getPosition())) {
			this.leftDefender = firstBlueRobot;
			this.leftAttacker = secondBlueRobot;
			if (this.rightDefenseZone.contains(firstYellowRobot.getPosition())) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.leftAttackZone.contains(firstBlueRobot.getPosition())) {
			this.leftAttacker = firstBlueRobot;
			this.leftDefender = secondBlueRobot;
			if (this.rightDefenseZone.contains(firstYellowRobot.getPosition())) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.rightDefenseZone.contains(firstBlueRobot.getPosition())) {
			this.rightDefender = firstBlueRobot;
			this.rightAttacker = secondBlueRobot;
			if (this.leftDefenseZone.contains(firstYellowRobot.getPosition())) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		} else {
			this.rightAttacker = firstBlueRobot;
			this.rightDefender = secondBlueRobot;
			if (this.leftDefenseZone.contains(firstYellowRobot.getPosition())) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		}
	}

	public void translate(StaticObject object) {
		Point translatedPt = CoordinateTranslator.fromVisionToPitch(object.getPosition(), this);
		object.setPosition(translatedPt);
	}

	public Rect getPitchRect(Point rectMember) {
       Rect result = new Rect(0.0, 1.0, 0.0, 1.0);
       List<Rect> rects = sections.getImportantRects();
       for (Rect rect : rects) {
    	   if (rect.contains(rectMember)) {
    		   result = rect;
    	   }
        }
       return result;
	}

}
