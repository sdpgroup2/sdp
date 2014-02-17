package sdp.group2.ai;

/**
 * @author Jaroslaw Hirniak 
 * */

import sdp.group2.world.IPitch;

public class Planner
{
	protected IPitch pitch;
	
	public Planner(IPitch pitch)
	{ this.pitch = (IPitch) pitch; }
	
	public IPitch getPitch()
	{ return pitch; }
}
