package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;

public class Pitch{
	
	private PitchLines lines;
	private PitchSection leftAttack;
	private PitchSection rightAttack;
	private PitchSection leftDefense;
	private PitchSection rightDefense;
	
	public Pitch(PitchSection LeftAttack, PitchSection RightAttack, 
			     PitchSection LeftDefense, PitchSection RightDefense){
		
		this.leftAttack = LeftAttack;
		this.rightAttack = RightAttack;
		this.leftDefense = LeftDefense;
		this.rightDefense = RightDefense;
		
		
	}
	
	public Rect getPitchRect() {
		return lines.getLinesRect();
	}
}
