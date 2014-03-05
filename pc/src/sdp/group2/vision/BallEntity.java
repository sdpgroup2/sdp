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

import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class BallEntity extends Entity {

//    Pitch 2:
//    private int[] mins = new int[]{-10, 92, 140};
//    private int[] maxs = new int[]{10, 256, 256};
	
	  private int[] mins = new int[]{-10, 80, 70};
	  private int[] maxs = new int[]{10, 256, 256};

	
    private static IplImage[] hsvImages = new IplImage[3];


    @Override
    public IplImage threshold(IplImage hsvImage) {
        for (int i = 0; i < 3; ++i) {
            hsvImages[i] = newImage(hsvImage, 1);
        }
        // Split 3-channel image into 3 1-channel images
        cvSplit(hsvImage, hsvImages[0], hsvImages[1], hsvImages[2], null);
        IplImage binaryImage = newImage(hsvImage, 1);
        IplImage tempImage = newImage(hsvImage, 1);

        for (int i = 0; i < 3; ++i) {
            int min = mins[i];
            int max = maxs[i];

            if (i == 0) {
                if (min < 0) {
                    // This is a workaround for hue because it's circular
                    cvThreshold(hsvImages[i], binaryImage, 180 + min, 255.0, CV_THRESH_BINARY);
                    cvThreshold(hsvImages[i], tempImage, max, 255.0, CV_THRESH_BINARY_INV);
                    cvOr(tempImage, binaryImage, binaryImage, null);
                } else {
                    cvInRangeS(hsvImages[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), binaryImage);
                }
            } else {
                cvInRangeS(hsvImages[i], cvScalar(min, 0, 0, 0), cvScalar(max, 0, 0, 0), tempImage);
                cvAnd(binaryImage, tempImage, binaryImage, null);
            }
        }
        // Erode here to remove all the small white pixel chunks
        cvErode(binaryImage, binaryImage, null, 3);
        cvDilate(binaryImage, binaryImage, null, 1);
        return binaryImage;
    }
    
}
