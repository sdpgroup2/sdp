package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

public class Pitch {
	
	private PitchLines lines;
	private PitchSection leftAttack;
	private PitchSection rightAttack;
	private PitchSection leftDefense;
	private PitchSection rightDefense;
	
	public Pitch(PitchSection leftAttack, PitchSection rightAttack, 
			     PitchSection leftDefense, PitchSection rightDefense) {
		this.leftAttack = leftAttack;
		this.rightAttack = rightAttack;
		this.leftDefense = leftDefense;
		this.rightDefense = rightDefense;
	}
	
	public Rect getPitchRect() {
		return lines.getLinesRect();
	}
}
