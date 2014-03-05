package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static sdp.group2.vision.ImageProcessor.newImage;

import java.util.List;

import sdp.group2.geometry.Point;


import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class DotEntity extends Entity {


	// MAIN PITCH
//  private int[] mins = new int[] {30, 30, 100};
//  private int[] maxs = new int[] {90, 155, 155};
	
    private int[] mins = new int[] {26, 55, 40};
    private int[] maxs = new int[] {100, 210, 60};
	
	// SIDE PITCH
//    private int[] mins = new int[] {30, 22, 50};
//    private int[] maxs = new int[] {80, 130, 140};
	
	// Main pitch at midnight
//	Minimum color (HSB): (16, 59, 45)
//	Maximum color (HSB): (97, 198, 70)

    @Override
    public IplImage threshold(IplImage hsvImage) {
        IplImage binaryImage = newImage(hsvImage, 1);
        cvInRangeS(hsvImage, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), binaryImage);
        cvDilate(binaryImage, binaryImage, null, 1);
        return binaryImage;
    }
    
    public void threshold(IplImage hsvImage, IplImage binaryImage) {
        cvInRangeS(hsvImage, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), binaryImage);
		cvDilate(binaryImage, binaryImage, null, 1);
    }
    
    /**
     * Finds a centroid (only one) for a blob in a binary image.
     * Used for the robot dot to select the centroid closes to the center.
     * @param binaryImage image to be searched
     * @return the centroid point
     */
    public Point findClosestCentroid(IplImage binaryImage, int areaMin, int areaMax, Point origPoint) {
    	List<Point> possibleCentroids = findPossibleCentroids(binaryImage, areaMin, areaMax, Integer.MAX_VALUE);
//    	System.out.println(possibleCentroids);
    	if (possibleCentroids.size() == 0) {
    		return null;
    	}
		// Maybe do some filtering - for now just return first
		Point minPoint = null;
		double minDist = Integer.MAX_VALUE;
//		System.out.println("------------------------");
		for (int i = 0; i < possibleCentroids.size(); i++) {
			Point centroid = possibleCentroids.get(i);
			double dist = centroid.distance(origPoint);
//			System.out.println(dist);
			if (dist < minDist) {
				minPoint = centroid;
				minDist = dist; 
			}
		}
//		System.out.println(minDist);
//		System.out.println("------------------------");
//		System.out.println(origPoint);
//		System.out.println(minPoint);
		return minPoint;
    }
}
