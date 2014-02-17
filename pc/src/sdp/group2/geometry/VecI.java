package sdp.group2.geometry;

/**
 * An immutable, 2-dimensional integer vector type.
 *
 * @author Paul Harris
 *
 */
public class VecI {

	public static final VecI[] OFFSETS = new VecI[] {
		new VecI( 1,  0),
		new VecI( 0,  1),
		new VecI(-1,  0),
		new VecI( 0, -1),
	};


	public final int x;
	public final int y;

	public VecI(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public VecI add(VecI other) {
		return new VecI(this.x+other.x, this.y+other.y);
	}

	public VecI sub(VecI other) {
		return new VecI(this.x-other.x, this.y-other.y);
	}

	public VecI mul(int c) {
		return new VecI(this.x*c, this.y*c);
	}

	@Override
	public String toString() {
		return "("+x+", "+y+")";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof VecI) {
			VecI v = (VecI) other;
			return (this.x == v.x && this.y == v.y);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (y << 16) + x;
	}
}
