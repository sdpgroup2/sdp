package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvOr;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSplit;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;
import static sdp.group2.vision.ImageProcessor.newImage;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class DotEntity extends Entity {

    private int[] mins = new int[]{27, 0, 5};
    private int[] maxs = new int[]{147, 37, 44};    // Needs to be configured

    private CvMemStorage storage = CvMemStorage.create();


    public IplImage threshold(IplImage image) {
        IplImage result = newImage(image, 1);

        cvInRangeS(image, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), result);
        cvErode(result, result, null, 1);
        cvDilate(result, result, null, 7);
        return result;
    }


	@Override
	public IplImage detect(IplImage[] hsvImages) {
		// TODO Auto-generated method stub
		return null;
	}
}
