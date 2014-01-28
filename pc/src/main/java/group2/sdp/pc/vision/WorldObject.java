package group2.sdp.pc.vision;

import group2.sdp.pc.geom.VecF;


/**
 * A representation of a physical object. The position and previous position
 * are both stored so that a future position can be predicted.
 * 
 * @author Paul Harris
 */
public class WorldObject {

	protected String name;
	protected VecF currentPosition;
	protected VecF previousPosition;
	protected boolean initialized = false;
	
	
	public WorldObject(String name, VecF position) {
		this.name = name;
		initialize(position);
	}
	
	public WorldObject(String name) {
		this.name = name;
	}
	
	public void initialize(VecF position) {
		currentPosition = position;
		previousPosition = position;
		initialized = true;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public VecF getPosition() {
		return currentPosition;
	}
	
	public VecF getVelocity() {
		return currentPosition.sub(previousPosition);
	}
	
	public VecF predictedPosition() {
		return currentPosition.add(getVelocity());
	}
	
	public void setPosition(VecF position) {
		previousPosition = currentPosition;
		currentPosition = position;
	}
	
}
