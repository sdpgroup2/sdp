package sdp.group2.geometry;

public class Millimeter {

	public double value;
	
	public Millimeter(double value) {
		this.value = value;
	}
	
	public int getPixels() {
		return mm2pix(value);
	}
	
	public void fromPixels(int pix) {
		value = pix2mm(pix);
	}
	
	public static double pix2mm(double pix) {
		return 0.259843661 * pix;
	}
	
	public static int mm2pix(double mm) {
		return (int) (mm * 0.259843661 + 0.5);
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

