package sdp.group2.geometry;

public class Milimeter {

	public double value;
	
	public Milimeter(double value) {
		this.value = value;
	}
	
	public int getPixels() {
		return mm2pix(value);
	}
	
	public void fromPixels(int pix) {
		value = pix2mm(pix);
	}
	
	public static double pix2mm(int pix) {
		return 0.259843661 * pix;
	}
	
	public static int mm2pix(double mm) {
		return (int) (mm * 0.259843661 + 0.5);
	}
	
}
