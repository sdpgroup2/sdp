package sdp.group2.geometry;

public class Millimeter {

    /**
     * Converts a pixel value to a millimeter value.
     * @param pix value in pixels.
     * @return value in millimeters.
     */
	public static double pix2mm(double pix) {
		return pix / 0.259843661;
	}

    /**
     * Converts a millimeter value to a pixel value.
     * @param mm value in millimeters.
     * @return value in pixels.
     */
	public static double mm2pix(double mm) {
		return  mm * 0.259843661;
	}
	
	public static void pix2mmInPlace(Point p) {
		p.x = pix2mm(p.x);
		p.y = pix2mm(p.y);
	}
	
	public static void pix2mmInPlace(PointSet ps) {
		for (Point p : ps.getPoints()) {
			pix2mmInPlace(p);
		}
	}
	
}

