package group2.sdp.pc.ai;

import group2.sdp.pc.world.IPitch;

public class DefensivePlanner extends Planner {

	private byte defenseZone;
	
	public DefensivePlanner(IPitch pitch, byte zoneId)
	{ 
		super(pitch);
		this.defenseZone = zoneId;
	}

	public void setDefenseZone(byte id)
	{
		getPitch().
	}
	
}
