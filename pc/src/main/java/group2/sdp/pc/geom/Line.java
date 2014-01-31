package group2.sdp.pc.geom;

public class Line {

	/**
	 * A 2-dimensional line defined by two vector points.
	 */
	
	private VecI[] points;
	
	public Line(VecI point0, VecI point1) {
		this.points = new VecI[2];
		points[0] = point0;
		points[1] = point1;
	}
	
}
