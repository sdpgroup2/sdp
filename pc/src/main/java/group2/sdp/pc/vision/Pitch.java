package group2.sdp.pc.vision;

import group2.sdp.pc.geom.Rect;

public class Pitch{
	
	private PitchLines lines;
	private PitchSection LeftAttack;
	private PitchSection RightAttack;
	private PitchSection LeftDefense;
	private PitchSection RightDefense;
	
	public Pitch(){
		
	}
	
	public Rect getPitchRect() {
		return lines.getLinesRect();
	}
}
