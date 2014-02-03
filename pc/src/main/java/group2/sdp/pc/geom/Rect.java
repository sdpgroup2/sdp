package group2.sdp.pc.geom;

public class Rect {
	
	public final int minX;
	public final int maxX;
	public final int minY;
	public final int maxY;
	public final int width;
	public final int height;
	
	public Rect(int minX, int maxX, int minY, int maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		width = maxX-minX+1;
		height = maxY-minY+1;
	}
	
	public VecI getCentre() {
		return new VecI((minX+maxX)/2, (minY+maxY)/2);
	}
	
	@Override
	public String toString() {
		return "Rect(("+minX+", "+minY+") to ("+maxX+", "+maxY+"))";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Rect) {
			Rect r = (Rect) other;
			return (minX == r.minX && maxX == r.maxX && minY == r.minY && maxY == r.maxY);
		}
		return false;
	}
	
}
