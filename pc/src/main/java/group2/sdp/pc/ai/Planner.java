
/** @author Jaroslaw Hirniak */

package group2.sdp.pc.ai;

import group2.sdp.pc.world.IPitch;

public class Planner
{
	protected IPitch pitch;
	
	public Planner(IPitch pitch)
	{ this.pitch = (IPitch) pitch; }
	
	public IPitch getPitch()
	{ return pitch; }
}
