package group2.sdp.pc.geom;

/**
 * An immutable, 2-dimensional float vector type.
 * 
 * @author Paul Harris
 * 
 */
public class VecF {
	
	public static final VecF[] OFFSETS = new VecF[] {
		new VecF( 1.0f,  0.0f),
		new VecF( 0.0f,  1.0f),
		new VecF(-1.0f,  0.0f),
		new VecF( 0.0f, -1.0f),
	};

	
	public final float x;
	public final float y;
	
	public VecF(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public VecF add(VecF other) {
		return new VecF(this.x+other.x, this.y+other.y);
	}
	
	public VecF sub(VecF other) {
		return new VecF(this.x-other.x, this.y-other.y);
	}
	
	public VecF mul(int c) {
		return new VecF(this.x*c, this.y*c);
	}
	
	public VecI toInts() {
		return new VecI(Math.round(x), Math.round(y));
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof VecF) {
			VecF v = (VecF) other;
			return (this.x == v.x && this.y == v.y);
		}
		return false;
	}
}
