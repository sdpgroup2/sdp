package group2.sdp.pc.ai;

import group2.sdp.pc.strategy.Zone;
import group2.sdp.pc.world.IPitch;

public class DefensivePlanner extends Planner {

	private Zone defenseZone;
	
	public DefensivePlanner(IPitch pitch, byte zoneId)
	{ 
		super(pitch);
		this.defenseZone = getPitch().getZone(zoneId);
	}

	public void intercept()
	{
		// if (defenseZone.contains())
	}
	
}
