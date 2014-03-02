package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static sdp.group2.vision.ImageProcessor.newImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class DotEntity extends Entity {

    private int[] mins = new int[] {30, 51, 102};		// dot min
    private int[] maxs = new int[] {45, 130, 140};    // Needs to be configured

    @Override
    public IplImage threshold(IplImage image) {
        IplImage result = newImage(image, 1);
        cvInRangeS(image, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), result);
        cvDilate(result, result, null, 3);
        return result;
    }
    
    public void threshold(IplImage image, IplImage target) {
        cvInRangeS(image, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), target);
		cvDilate(target, target, null, 3);
    }

}
