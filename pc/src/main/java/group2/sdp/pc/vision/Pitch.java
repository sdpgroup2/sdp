package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;

import java.util.List;

public class Pitch {
	
	private PitchLines lines;
	private PitchSection sections;
	
	private Rect pitchRect;
	private Rect leftAttack;
	private Rect rightAttack;
	private Rect leftDefense;
	private Rect rightDefense;
	
	private Rect leftDefender = null;
	private Rect leftAttacker = null;
	private Rect rightDefender = null;
	private Rect rightAttacker = null;

	private Rect ball;
	
	private final VecI LEFT_DEF_POINT = new VecI(120,240);
	private final VecI LEFT_ATT_POINT = new VecI(270,240);
	private final VecI RIGHT_DEF_POINT = new VecI(370,240);
	private final VecI RIGHT_ATT_POINT = new VecI(410,240);
	
	public Pitch(PitchLines lines, PitchSection sections) {
		this.lines = lines;
		this. sections = sections;
		
		pitchRect    = getPitchRect();
		leftDefense  = getPitchRect(LEFT_DEF_POINT);
		leftAttack   = getPitchRect(LEFT_ATT_POINT);
		rightDefense = getPitchRect(RIGHT_DEF_POINT);
		rightAttack  = getPitchRect(RIGHT_ATT_POINT);
		
	}
	
	public Rect getPitchRect(VecI rectMember) {
		
		Rect result = new Rect(0,1,0,1);
		List<Rect> rects = sections.getImportantRects();
		for(Rect rect : rects) {
			if(rectMember.x > rect.minX && rectMember.x < rect.maxX
			   && rectMember.y >rect.minY && rectMember.y < rect.maxY) {
				result = rect;
			}
		}
		
		return result;
	}
	
	public void addBall(Rect ball) {
		this.ball = ball;
	}
	
	/**
	 * Adds the robot to the pitch model, depending on the location of their rectangle.
	 * @param blueRobots - List of rectangles of blue robots.
	 * @param yellowRobots - List of rectangles of yellow robots.
	 */
	public void addRobots(List<Rect> blueRobots, List<Rect> yellowRobots) {
		Rect firstBlueRobot = blueRobots.get(0);
		Rect secondBlueRobot = blueRobots.get(1);
		Rect firstYellowRobot = yellowRobots.get(0);
		Rect secondYellowRobot = yellowRobots.get(1);
		
		if (this.leftDefense.getPitchRect().contains(firstBlueRobot)) {
			this.leftDefender = firstBlueRobot;
			this.leftAttacker = secondBlueRobot;
			if (this.rightDefense.getPitchRect().contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.leftAttack.getPitchRect().contains(firstBlueRobot)) {
			this.leftAttacker = firstBlueRobot;
			this.leftDefender = secondBlueRobot;
			if (this.rightDefense.getPitchRect().contains(firstYellowRobot)) {
				this.rightDefender = firstYellowRobot;
				this.rightAttacker = secondYellowRobot;
			}
		} else if (this.rightDefense.getPitchRect().contains(firstBlueRobot)) {
			this.rightDefender = firstBlueRobot;
			this.rightAttacker = secondBlueRobot;
			if (this.leftDefense.getPitchRect().contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		} else {
			this.rightAttacker = firstBlueRobot;
			this.rightDefender = secondBlueRobot;
			if (this.leftDefense.getPitchRect().contains(firstYellowRobot)) {
				this.leftDefender = firstYellowRobot;
				this.leftAttacker = secondYellowRobot;
			}
		}
	}
	
	public Rect getPitchRect() {
		return lines.getImportantRects().get(0);
	}
}
