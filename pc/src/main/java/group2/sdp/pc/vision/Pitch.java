package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

import java.util.List;

public class Pitch{
	
	private PitchLines lines;
	private PitchSection LeftAttack;
	private PitchSection RightAttack;
	private PitchSection LeftDefense;
	private PitchSection RightDefense;
	
	public Pitch(PitchSection LeftAttack, PitchSection RightAttack, 
			     PitchSection LeftDefense, PitchSection RightDefense){
		
		this.LeftAttack = LeftAttack;
		this.RightAttack = RightAttack;
		this.LeftDefense = LeftDefense;
		this.RightDefense = RightDefense;
		
		
	}
	
	public Rect getPitchRect() {
		return lines.getLinesRect();
	}
}
