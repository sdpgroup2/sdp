package sdp.group2.ai;

import sdp.group2.world.IPitch;

/**
 * @author Jaroslaw Hirniak
 * */

public abstract class Planner {
	protected IPitch pitch;

	public Planner(IPitch pitch) {
		this.pitch = (IPitch) pitch;
	}

	public IPitch getPitch() {
		return pitch;
	}
}
