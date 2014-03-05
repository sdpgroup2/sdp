package sdp.group2.world;

import sdp.group2.geometry.*;
import sdp.group2.world.Robot;


public class Zone extends Plane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	
	public Zone(int id) {
		super(Integer.toString(id));
		this.id = id;
	}

	public int getID() {
		return id;
	}
	
}
