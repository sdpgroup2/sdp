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
	
	private static Rect yellowDefender;
	private static Rect yellowAttacker;
	private static Rect blueDefender;
	private static Rect blueAttacker;
	
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
	
	public Rect getPitchRect() {
		return lines.getImportantRects().get(0);
	}
}
